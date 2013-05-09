#include "starj_setup.h"
#include "starj_spec.h"
#include "starj_util.h"
#include "starj_class_resolver.h"
#include "starj_id_resolver.h"

#include "../data/starj_data_util.h"

#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <errno.h>
#include <libgen.h>

#include <stdio.h>

typedef struct {
    char *name;
    char *description;
    int kind;
    union {
        char **s;
        struct {
            char ***array;
            int *length;
        } cp;
        struct {
            sjat_field_mask *event_masks;
            sjat_field_mask mask;
        } mask;
        bool *b;
        int *i;
    } value;
} sjat_option;

static sjat_option **options;

static char *help_request;

char eventToChar[STARJ_EVENT_COUNT] = {
    'A', 'a', 'c', 'k', 'C', 'l', 'L', 'q', 'Q',
    'G', 'g', 'h', 'j', 'J', 'w', 'W', 'v', 'V', 'b',
    'm', 'M', 'd', 'e', 'D', 'x', 'y', 'Y', 'o', 'p',
    'O', 'P', 'r', 'E', 'R', 'T', 't', 'i'/*, 'z'*/
};

char charToEvent[((1 << (sizeof(char) * 8 - 1)) - 1)];

sjat_option *sjaf_alloc_string_option(char *name, char *description,
        char **value) {
    sjat_option *result = SJAM_NEW(sjat_option);
    result->name = name;
    result->description = description;
    result->kind = STARJ_OPT_TYPE_STRING;
    result->value.s = value;
    
    return result;
}

sjat_option *sjaf_alloc_int_option(char *name, char *description, int *value) {
    sjat_option *result = SJAM_NEW(sjat_option);
    result->name = name;
    result->description = description;
    result->kind = STARJ_OPT_TYPE_INT;
    result->value.i = value;
    
    return result;
}

sjat_option *sjaf_alloc_size_option(char *name, char *description, int *value) {
    sjat_option *result = SJAM_NEW(sjat_option);
    result->name = name;
    result->description = description;
    result->kind = STARJ_OPT_TYPE_SIZE;
    result->value.i = value;
    
    return result;
}

sjat_option *sjaf_alloc_verbosity_option(char *name, char *description,
        int *value) {
    sjat_option *result = SJAM_NEW(sjat_option);
    result->name = name;
    result->description = description;
    result->kind = STARJ_OPT_TYPE_VERBOSITY;
    result->value.i = value;
    
    return result;
}

sjat_option *sjaf_alloc_bool_option(char *name, char *description,
        bool *value) {
    sjat_option *result = SJAM_NEW(sjat_option);
    result->name = name;
    result->description = description;
    result->kind = STARJ_OPT_TYPE_BOOL;
    result->value.b = value;
    
    return result;
}

sjat_option *sjaf_alloc_classpath_option(char *name, char *description,
        char ***array, int *length) {
    sjat_option *result = SJAM_NEW(sjat_option);
    result->name = name;
    result->description = description;
    result->kind = STARJ_OPT_TYPE_CLASSPATH;
    result->value.cp.array = array;
    result->value.cp.length = length;
    
    return result;
}

sjat_option *sjaf_alloc_field_mask_option(char *name,
        char *description, sjat_field_mask *event_masks, sjat_field_mask mask) {
    sjat_option *result = SJAM_NEW(sjat_option);
    result->name = name;
    result->description = description;
    result->kind = STARJ_OPT_TYPE_EVENT_MASK;
    result->value.mask.event_masks = event_masks;
    result->value.mask.mask = mask;
    
    return result;
}

/* -------------------------------------------------------------------------- *
 *                               getNextOption                                *
 *  ------------------------------------------------------------------------  *
 *                                                                            *
 *  Parses a string to find the next option. The option name is stored into   *
 *  'option' and its value in 'value' (if provided). 'src' is advanced to     *
 *  to allow further calls to getNextOption() to return the next option       *
 *  values.                                                                   *
 *                                                                            *
 *  Parameters:                                                               *
 *      + src: a pointer to the string to be parsed                           *
 *      + option: an array of characters that receives the option name        *
 *      + value: an array of characters that receives the option value, if    *
 *               any                                                          *
 *                                                                            *
 *   Return value: 0 is no option is available, 1 otherwise.                  *
 *                                                                            *
 * -------------------------------------------------------------------------- */
int getNextOption(char **src, char *option, char *value) {
    char *p = *src;
    char *q = option;

    if ((src == NULL) || (*src == NULL) || (**src == '\0')) {
        return 0;
    }
    
    *option = '\0';
    *value = '\0';
    while (true) {
        switch (*p) {
            case '=':
                p++;
                *q = '\0';
                q = value;
                break;
            case ',':
                p++;
                *q = '\0';
                *src = p;
                return 1;
            case '\0':
                *q = *p;
                *src = p;
                return 1;
            default:
                *q = *p;
                p++;
                q++;
                break;
        }
    }
}

/* -------------------------------------------------------------------------- *
 *                               splitClassPath                               *
 *  ------------------------------------------------------------------------  *
 *                                                                            *
 *  Converts a string representing a class path to an array of strings, each  *
 *  representing an element of the class path.                                *
 *                                                                            *
 *  Parameters:                                                               *
 *      + src: a pointer to the string to be parsed                           *
 *      + delim: the character which is used to delimit the elements in the   *
 *               the path                                                     *
 *      + env: determines whether the value of the CLASSPATH environment      *
 *             variable will be added to the given class path, and where it   *
 *             will be inserted.                                              *
 *      + dest: a pointer to an array of strings which will hold the          *
 *              elements. The array will be dynamically allocated.            *
 *                                                                            *
 *   Return value: the length of the newly created dest array                 *
 *                 or < 0 on error                                            *
 *                                                                            *
 * -------------------------------------------------------------------------- */
int splitClassPath(char *src, char delim, sjat_env_classpath_option env,
        char ***dest) {
    char *cp;
    char *p, *q;
    char *env_cp;
    int num_entries = 0;
    int current_index;
    size_t src_len;
    size_t env_len;
    char **result = NULL;

    /* Get the value of the CLASSPATH environment variable */
    env_cp = (env != NO_ENV_CP ? getenv("CLASSPATH") : NULL);

    /* Concatenate the two strings, if needed */
    src_len = (src != NULL ? strlen(src) : 0);
    env_len =  (env_cp != NULL ? strlen(env_cp) : 0);
    cp = SJAM_NEW_ARRAY(char, (src_len + env_len + 2));
    if (cp == NULL) {
        sjaf_error("Failed to allocate memory for CP string concatenation");
        return STARJ_ERR_MALLOC;
    }

    switch (env) {
        case NO_ENV_CP:
            if (src == NULL) {
                goto finalize;
            }
            strcpy(cp, src);
            break;
        case ENV_CP_PREPEND:
            strcpy(cp, env_cp);
            cp[env_len] = delim;
            cp[env_len + 1] = '\0';
            if (src != NULL) {
                strcpy(cp + env_len + 1, src);
            }
            break;
        case ENV_CP_APPEND:
            if (src != NULL) {
                strcpy(cp, src);
                cp[src_len] = delim;
                cp[src_len + 1] = '\0';
            }
            strcpy(cp + src_len + 1, env_cp);
            break;
    }

    /* Compute the number of entries in the class path */
    if (cp == NULL) {
        goto finalize;
    }

    num_entries = 1;
    for (p = cp; *p; p++) {
        if (*p == delim) {
            num_entries++;
        }
    }

    /* Split the class path */
    result = SJAM_NEW_ARRAY(char *, num_entries);
    if (result == NULL) {
        sjaf_error("Failed to allocate memory for CP");
        return STARJ_ERR_MALLOC;
    }

    p = q = cp;
    current_index = 0;
    while (true) {
        if ((*q == delim) || (*q == '\0')) {
            char *buff;

            buff = SJAM_NEW_ARRAY(char, (q - p + 1));
            strncpy(buff, p, q - p);
            buff[q - p] = '\0';
            result[current_index++] = buff;
            sjaf_debug("Added classpath entry: \"%s\"", buff);
            p = q + 1;

            if (*q == '\0') {
                break;
            }
        }
        q++;
    }
    
finalize:
    *dest = result;
    return num_entries;
}

/* -------------------------------------------------------------------------- *
 *                                 printEvents                                *
 *  ------------------------------------------------------------------------  *
 *                                                                            *
 *   Displays the agent's event character table to 'stdout'.                  *
 *                                                                            *
 * -------------------------------------------------------------------------- */
void printEvents() {
    int i;

    fprintf(stdout, "*J Agent Event Table\n");
    fprintf(stdout, "===================\n");
    for (i = 0; i < STARJ_EVENT_COUNT; i++) {
        fprintf(stdout, "  %c  %s\n", eventToChar[i], sjav_event_names[i]);
    }
}
/* -------------------------------------------------------------------------- *
 *                                 printUsage                                 *
 *  ------------------------------------------------------------------------  *
 *                                                                            *
 *   Displays the agent's usage string on 'stdout'.                           *
 *                                                                            *
 * -------------------------------------------------------------------------- */
void printUsage() {
    int i;
    char buffer[1024];


    fprintf(stdout, "*J version %s\n", STARJ_VERSION);
    fprintf(stdout, "Author: %s (%s)\n", STARJ_AUTHOR, STARJ_AUTHOR_EMAIL);
    fprintf(stdout, "URL: %s\n", STARJ_URL);
    fprintf(stdout, "\n");
    fprintf(stdout, "*J Agent Options\n");
    fprintf(stdout, "===================\n");

    for (i = 0; i < STARJ_OPT_COUNT; i++) {
        sjat_option* opt = options[i];
        if (opt != NULL) {
            char *s = NULL;
            if (opt->description == NULL) {
                opt->description = "";
            }
 
            strcpy(buffer, opt->name);
            switch (opt->kind) {
                case STARJ_OPT_TYPE_BOOL:
                    s = "=<bool>";
                    break;
                case STARJ_OPT_TYPE_INT:
                    s = "=<int>";
                    break;
                case STARJ_OPT_TYPE_SIZE:
                    s = "=<size>";
                    break;
                case STARJ_OPT_TYPE_STRING:
                    s = "=<string>";
                    break;
                case STARJ_OPT_TYPE_CLASSPATH:
                    s = "=<classpath>";
                    break;
                case STARJ_OPT_TYPE_VERBOSITY:
                    s = "=<verbosity_level>";
                    break;
                case STARJ_OPT_TYPE_EVENT_MASK:
                    s = "=<event_char>+";
                    break;
                default:
                    fprintf(stdout, "!!\n");
                    sjaf_error("Unknown option kind: %d", opt->kind);
                    sjaf_profiler_exit(STARJ_ERR_UNKNOWN_OPTION_KIND);
                    break;
            }
            strcat(buffer, s);
            fprintf(stdout, "%-26s %s\n", buffer, opt->description);
        }
    }
    fprintf(stdout, "\n");
}

/* ========================================================================== */
void release_hash_set(sjdt_value value) {
    sjdf_hash_set_release((sjdt_hash_set *) value);
}

void initializeSetsAndMaps() {
    int result;
    
    /* Object IDs */
    result = sjdf_hash_set_init(&sjav_known_object_ids, /* Set */
            1023,   /* Initial size */
            0.75,  /* Load factor */
            NULL,  /* Hash function (NULL = default) */
            NULL,  /* Comparison function (NULL = default) */
            NULL,  /* Clone function (NULL = default) */
            NULL); /* Release function (NULL = default) */
    if (result != STARJ_DATA_OK) {
        sjaf_error("Failed to initialize object set");
        sjaf_profiler_exit(result);
    }

    /* Method IDs */
    result = sjdf_hash_set_init(&sjav_known_method_ids, /* Set */
            255,   /* Initial size */
            0.75,  /* Load factor */
            NULL,  /* Hash function (NULL = default) */
            NULL,  /* Comparison function (NULL = default) */
            NULL,  /* Clone function (NULL = default) */
            NULL); /* Release function (NULL = default) */
    if (result != STARJ_DATA_OK) {
        sjaf_error("Failed to initialize method set");
        sjaf_profiler_exit(result);
    }

    /* Thread IDs */
    result = sjdf_hash_set_init(&sjav_known_thread_ids, /* Set */
            7,     /* Initial size */
            0.75,  /* Load factor */
            NULL,  /* Hash function (NULL = default) */
            NULL,  /* Comparison function (NULL = default) */
            NULL,  /* Clone function (NULL = default) */
            NULL); /* Release function (NULL = default) */
    if (result != STARJ_DATA_OK) {
        sjaf_error("Failed to initialize thread set");
        sjaf_profiler_exit(result);
    }

    /* Pending Object IDs */
    result = sjdf_hash_set_init(&sjav_pending_object_ids, /* Set */
            7,     /* Initial size */
            0.75,  /* Load factor */
            NULL,  /* Hash function (NULL = default) */
            NULL,  /* Comparison function (NULL = default) */
            NULL,  /* Clone function (NULL = default) */
            NULL); /* Release function (NULL = default) */
    if (result != STARJ_DATA_OK) {
        sjaf_error("Failed to initialize pending object set");
        sjaf_profiler_exit(result);
    }

    /* Pending Class IDs */
    result = sjdf_hash_set_init(&sjav_pending_class_ids, /* Set */
            7,     /* Initial size */
            0.75,  /* Load factor */
            NULL,  /* Hash function (NULL = default) */
            NULL,  /* Comparison function (NULL = default) */
            NULL,  /* Clone function (NULL = default) */
            NULL); /* Release function (NULL = default) */
    if (result != STARJ_DATA_OK) {
        sjaf_error("Failed to initialize pending class set");
        sjaf_profiler_exit(result);
    }

    /* Pending Thread IDs */
    result = sjdf_hash_set_init(&sjav_pending_thread_ids, /* Set */
            7,     /* Initial size */
            0.75,  /* Load factor */
            NULL,  /* Hash function (NULL = default) */
            NULL,  /* Comparison function (NULL = default) */
            NULL,  /* Clone function (NULL = default) */
            NULL); /* Release function (NULL = default) */
    if (result != STARJ_DATA_OK) {
        sjaf_error("Failed to initialize pending thread set");
        sjaf_profiler_exit(result);
    }

    /* Arena IDs */
    result = sjdf_hash_set_init(&sjav_known_arena_ids, /* Set */
            7,     /* Initial size */
            0.75,  /* Load factor */
            NULL,  /* Hash function (NULL = default) */
            NULL,  /* Comparison function (NULL = default) */
            NULL,  /* Clone function (NULL = default) */
            NULL); /* Release function (NULL = default) */
    if (result != STARJ_DATA_OK) {
        sjaf_error("Failed to initialize arena set");
        sjaf_profiler_exit(result);
    }

    /* Class ID -> Methods */
    result = sjdf_hash_map_init(&sjav_class_id_to_methods, /* Map */
            255,   /* Initial Size */
            0.75,  /* Load factor */
            NULL,  /* Hash function (NULL = default) */
            NULL,  /* Key comparison function (NULL = default) */
            NULL,  /* Key clone function (NULL = default) */
            NULL,  /* Key release function (NULL = default) */
            NULL,  /* Value comparison function (NULL = default) */
            NULL,  /* Value clone function (NULL = default) */
            release_hash_set); /* Value release function (NULL = default) */
    if (result != STARJ_DATA_OK) {
        sjaf_error("Failed to initialize class ID map");
        sjaf_profiler_exit(result);
    }

    /* Arena ID -> Objects */
    result = sjdf_hash_map_init(&sjav_arena_id_to_objects, /* Map */
            7,   /* Initial Size */
            0.75,  /* Load factor */
            NULL,  /* Hash function (NULL = default) */
            NULL,  /* Key comparison function (NULL = default) */
            NULL,  /* Key clone function (NULL = default) */
            NULL,  /* Key release function (NULL = default) */
            NULL,  /* Value comparison function (NULL = default) */
            NULL,  /* Value clone function (NULL = default) */
            release_hash_set); /* Value release function (NULL = default) */
    if (result != STARJ_DATA_OK) {
        sjaf_error("Failed to initialize arena ID map");
        sjaf_profiler_exit(result);
    }

    /* Method ID -> Bytecode (cfrt_bytecode) */

    result = sjdf_hash_map_init(&sjav_method_id_to_bytecode, /* Map */
            255,   /* Initial Size */
            0.75,  /* Load factor */
            NULL,  /* Hash function (NULL = default) */
            NULL,  /* Key comparison function (NULL = default) */
            NULL,  /* Key clone function (NULL = default) */
            NULL,  /* Key release function (NULL = default) */
            NULL,  /* Value comparison function (NULL = default) */
            NULL,  /* Value clone function (NULL = default) */
            sjdf_generic_value_free); /* Value release function (NULL = default) */
}

/* ========================================================================== */

/* -------------------------------------------------------------------------- *
 *                                 initialize                                 *
 *  ------------------------------------------------------------------------  *
 *                                                                            *
 *  Initializes the option array with sjat_option structures and initializes   *
 *  all tables.                                                               *
 *                                                                            *
 * -------------------------------------------------------------------------- */
void initialize() {
    int i;
    jint max;
    
    /* Initialize the option array */
    options = SJAM_NEW_ARRAY(sjat_option *, STARJ_OPT_COUNT);
    for (i = 0; i < STARJ_OPT_COUNT; i++) {
        options[i] = NULL;
    }

    options[STARJ_OPT_FILE] = sjaf_alloc_string_option("file",
            "Specifies the name of the output file", &sjav_filename);
    options[STARJ_OPT_SPECFILE] = sjaf_alloc_string_option("specfile",
            "Specifies the name of the event specification file",
            &sjav_spec_filename);
    options[STARJ_OPT_EVENTS] = sjaf_alloc_field_mask_option("events",
            "Specifies a list of events to record", sjav_event_masks,
            STARJ_FIELD_ALL);
    options[STARJ_OPT_COUNTERS] = sjaf_alloc_field_mask_option("counters",
            "Specifies a list of events to count", sjav_event_masks,
            STARJ_FIELD_COUNTED);
    options[STARJ_OPT_SPLIT] = sjaf_alloc_size_option("split",
            "Specifies the file split threshold", &sjav_split_threshold);
#ifdef STARJ_ENABLE_PIPE
    options[STARJ_OPT_PIPE] = sjaf_alloc_bool_option("pipe",
            "Instructs the agent to output to a named pipe", &sjav_pipe_mode);
#endif
    options[STARJ_OPT_VERBOSE] = sjaf_alloc_verbosity_option("verbose",
            "Instructions the agent to produce additional output",
            &sjav_verbosity_level);
    options[STARJ_OPT_OPTIMIZE] = sjaf_alloc_bool_option("opt",
            "Instructs the agent to attempt to reduce the trace size",
            &sjav_optimize_mode);
    options[STARJ_OPT_CP] = sjaf_alloc_classpath_option("cp",
            "Specifies the agent's class path", &sjav_class_path,
            &sjav_class_path_len);
    options[STARJ_OPT_CP_APPEND] = sjaf_alloc_classpath_option("cp+",
            "Specifies a class path to append to the value of $CLASSPATH",
            &sjav_class_path, &sjav_class_path_len);
    options[STARJ_OPT_CP_PREPEND] = sjaf_alloc_classpath_option("+cp",
            "Specifies a class path to prepend to the value of $CLASSPATH",
            &sjav_class_path, &sjav_class_path_len);
#ifdef STARJ_USE_COLOURS
    options[STARJ_OPT_COLOURS] = sjaf_alloc_bool_option("colour",
            "Specifies whether or not colour should be used in the output",
            &sjav_colours);
#endif
    options[STARJ_OPT_BC_TAGS] = sjaf_alloc_bool_option("bctags",
            "Instructs the agent to output bytecode tags", &sjav_use_bc_tags);
    options[STARJ_OPT_GZIP] = sjaf_alloc_bool_option("gzip",
            "Instructs the agent to output a gzip'ped trace",
            &sjav_gzip_output);
    options[STARJ_OPT_UNIQUE_IDS] = sjaf_alloc_bool_option("unique_ids",
            "Instructs the agent to output unique identifiers",
            &sjav_unique_ids);
    options[STARJ_OPT_HELP] = sjaf_alloc_string_option("help",
            "Displays usage information", &help_request);

    /* Initialize the char -> event table */
    for (i = 0; i < (sizeof(charToEvent) / sizeof(char)); i++) {
        charToEvent[i] = STARJ_INVALID_EVENT;
    }

    /* Compute the maximal JVMPI event ID */
#ifdef JVMPI_MAX_EVENT_TYPE_VAL
    max = JVMPI_MAX_EVENT_TYPE_VAL;
#else
    max = JINT_MIN;
    for (i = 0; i < STARJ_EVENT_COUNT; i++) {
        jint t = sjav_event_to_jvmpi[i];
        if ((t != JVMPI_REQUESTED_EVENT) && (t > max)) {
            max = t;
        }
    }
#endif

    /* Allocate and initialize the JVMPI -> event table */
    sjav_jvmpi_to_event = SJAM_NEW_ARRAY(sjat_event, (max + 1));
    for (i = 0; i <= max; i++) {
        sjav_jvmpi_to_event[i] = STARJ_INVALID_EVENT;
    }
    
    /* Fill in the tables */
    for (i = 0; i < STARJ_EVENT_COUNT; i++) {
        char c = eventToChar[i];
        jint e  = sjav_event_to_jvmpi[i];
        charToEvent[(int)(c & 0x007F)] = (sjat_event) i;
        if (e <= max) {
            sjav_jvmpi_to_event[e] = (sjat_event) i;
        }
        sjav_event_counts[i] = 0L;
    }

    initializeSetsAndMaps();
}

void initializeModules() {
    sjaf_class_resolver_init();
    if (sjav_unique_ids) {
        sjaf_id_resolver_init();
    }
}

/* -------------------------------------------------------------------------- *
 *                             setOptionDefaults                              *
 *  ------------------------------------------------------------------------  *
 *                                                                            *
 *  Initializes global option variables to their default values.              *
 *                                                                            *
 * -------------------------------------------------------------------------- */
void setOptionDefaults() {
    sjav_out_stream = stderr;
    sjav_filename = STARJ_DEFAULT_FILENAME;
    sjav_spec_filename = NULL;
    sjav_split_threshold = 0;
    sjav_pipe_mode = false;
    sjav_verbosity_level = STARJ_VERBOSITY_DEFAULT;
    sjav_optimize_mode = true;
    sjav_class_path = NULL;
    sjav_class_path_len = 0;
    sjav_colours = isatty(fileno(sjav_out_stream));
    sjav_use_bc_tags = false;
    sjav_gzip_output = false;
    sjav_unique_ids = false;
    help_request = NULL;

    /* Debugging support */
    #ifdef STARJ_DBG_TRACE_EVENTS
        sjav_dbg_event_file = stderr;
    #endif
    #ifdef STARJ_DBG_TRACE_REQUESTS
        sjav_dbg_req_file = stderr;
    #endif
}

/* -------------------------------------------------------------------------- *
 *                               processOptions                               *
 *  ------------------------------------------------------------------------  *
 *                                                                            *
 *  Tokenizes and processes an option string, and sets appropriate options    *
 *  accordingly.                                                              *
 *                                                                            *
 *  Parameters:                                                               *
 *      + option_str: the agent's option string as provided by the JVMPI      *
 *                    interface                                               *
 *                                                                            *
 *  Side Effect: this function may exit the profiler if the provided option   *
 *               string is invalid.                                           *
 *                                                                            *
 * -------------------------------------------------------------------------- */
void processOptions(char *option_str) {
    char **p = &option_str;
    char *option;
    char *value;
    char *tmp_filename;
    size_t len;
    sjat_env_classpath_option env_cp;
    bool found;
    int i;
    
    if (option_str == NULL) {
        return;
    }

    /* Allocate two buffers which of sufficient size to store
     * the option name and its specified value */
    len = strlen(option_str) + 1;
    option = SJAM_NEW_ARRAY(char, len);
    value = SJAM_NEW_ARRAY(char, len);

    while (getNextOption(p, option, value)) {
        int i;
        found = false;
        for (i = 0; i < STARJ_OPT_COUNT; i++) {
            sjat_option* candidate = options[i];
            if ((candidate != NULL) && !strcmp(option, candidate->name)) {
                /* Found a match */
                found = true;
                /* sjaf_debug("Option \"%s\" specified with value \"%s\"", candidate->name, value); */
                
                switch (candidate->kind) {
                    case STARJ_OPT_TYPE_BOOL:
                        if (!sjaf_parse_bool(value, candidate->value.b)) {
                            sjaf_error("Invalid boolean value: \"%s\"", value);
                            sjaf_profiler_exit(STARJ_ERR_INVALID_BOOL);
                        }
                        break;
                    case STARJ_OPT_TYPE_INT:
                        if (!sjaf_parse_int(value, candidate->value.i)) {
                            sjaf_error("Invalid integer value: \"%s\"", value);
                            sjaf_profiler_exit(STARJ_ERR_INVALID_INT);
                        }
                        break;
                    case STARJ_OPT_TYPE_SIZE:
                        if (!sjaf_parse_size(value, candidate->value.i)) {
                            sjaf_error("Invalid size value: \"%s\"", value);
                            sjaf_profiler_exit(STARJ_ERR_INVALID_SIZE);
                        }
                        break;
                    case STARJ_OPT_TYPE_STRING:
                        *candidate->value.s = strdup(value);
                        break;
                    case STARJ_OPT_TYPE_CLASSPATH:
                        if (!strcmp(candidate->name, "cp+")) {
                            env_cp = ENV_CP_APPEND;
                        } else if (!strcmp(candidate->name, "+cp")) {
                            env_cp = ENV_CP_PREPEND;
                        } else {
                            env_cp = NO_ENV_CP;
                        }
                        *candidate->value.cp.length = splitClassPath(value,
                                ':', env_cp, candidate->value.cp.array);
                        break;
                    case STARJ_OPT_TYPE_VERBOSITY:
                        if (*value == '\0') {
                            *candidate->value.i = STARJ_VERBOSITY_HIGH;
                        } else if (!strcmp(value, "none")) {
                            *candidate->value.i = STARJ_VERBOSITY_NONE;
                        } else if (!strcmp(value, "err")) {
                            *candidate->value.i = STARJ_VERBOSITY_ERR;
                        } else if (!strcmp(value, "warn")) {
                            *candidate->value.i = STARJ_VERBOSITY_WARN;
                        } else if (!strcmp(value, "mess")) {
                            *candidate->value.i = STARJ_VERBOSITY_MESS;
                        } else if (!strcmp(value, "normal")) {
                            *candidate->value.i = STARJ_VERBOSITY_NORMAL;
                        } else if (!strcmp(value, "default")) {
                            *candidate->value.i = STARJ_VERBOSITY_DEFAULT;
                        } else if (!strcmp(value, "debug")) {
                            *candidate->value.i = STARJ_VERBOSITY_DEBUG;
                        } else if (!strcmp(value, "low")) {
                            *candidate->value.i = STARJ_VERBOSITY_LOW;
                        } else if (!strcmp(value, "high")) {
                            *candidate->value.i = STARJ_VERBOSITY_HIGH;
                        } else if (!strcmp(value, "all")) {
                            *candidate->value.i = STARJ_VERBOSITY_ALL;
                        } else {
                            char *c;
                            int verbosity = STARJ_VERBOSITY_NONE;
                            for (c = value; *c; c++) {
                                switch (*c) {
                                    case 'e':
                                        verbosity |= STARJ_VERBOSE_ERR;
                                        break;
                                    case 'w':
                                        verbosity |= STARJ_VERBOSE_WARN;
                                        break;
                                    case 'm':
                                        verbosity |= STARJ_VERBOSE_MESS;
                                        break;
                                    case 'd':
                                        verbosity |= STARJ_VERBOSE_DEBUG;
                                        break;
                                    case 'x':
                                        verbosity |= STARJ_VERBOSE_EXTRA;
                                        break;
                                    default:
                                        sjaf_error("Invalid verbosity specifier: \"%c\"", *c);
                                        sjaf_profiler_exit(STARJ_ERR_INVALID_VERBOSITY_CHAR);
                                }
                            }

                            *candidate->value.i = verbosity;
                        }
                        break;
                    case STARJ_OPT_TYPE_EVENT_MASK: {
                            char *c;

                            for (c = value; *c != '\0'; c++) {
                                sjat_event e = charToEvent[(int)(*c & 0x007F)];
                                if (e >= STARJ_EVENT_COUNT) {
                                    sjaf_error("Invalid event char specifier: '%c'", *c);
                                    sjaf_profiler_exit(STARJ_ERR_INVALID_EVENT_CHAR);
                                }

                                candidate->value.mask.event_masks[(int)(e & 0x7FFFFFFF)] |= candidate->value.mask.mask;
                            }
                        }
                        break;
                    default:
                        sjaf_error("Unknown option kind: %d", candidate->kind);
                        sjaf_profiler_exit(STARJ_ERR_UNKNOWN_OPTION_KIND);
                        break;
                }

                break;
            }
        }

        if (!found) {
            sjaf_error("Invalid option: \"%s\"", option);
            sjaf_profiler_exit(STARJ_ERR_UNKNOWN_OPTION);
        }
    }
    
    free(option);
    free(value);

    /* -- Take care of options that require additional processing -- */

    /* Spec file -- Has to be first since many other checks will examine
     * the values set by processing this option. */
    if (sjav_spec_filename != NULL) {
        int result;
        FILE *f = fopen(sjav_spec_filename, "rb");
        if (f == NULL){
            sjaf_error("Failed to open spec file \"%s\"", sjav_spec_filename);
            sjaf_profiler_exit(STARJ_ERR_IO);
        }

        result = sjaf_read_spec_file(f, sjav_event_masks, STARJ_EVENT_COUNT);
        fclose(f);
        
        if (result != STARJ_OK) {
            sjaf_profiler_exit(result);
        }
    }
    
#ifdef STARJ_ENABLE_PIPE
    if (sjav_pipe_mode) {
        for (i = 0; i < STARJ_EVENT_COUNT; i++) {
            if (SJAM_COUNTED(sjav_event_masks[i])) {
                sjaf_error("Pipe mode precludes the usage of event counters");
                sjaf_profiler_exit(STARJ_ERR_COUNTERS_AND_PIPE);
            }
        }
    }
#endif

    if (sjav_gzip_output) {
        for (i = 0; i < STARJ_EVENT_COUNT; i++) {
            if (SJAM_COUNTED(sjav_event_masks[i])) {
                sjaf_error("GZip mode precludes the usage of event counters");
                sjaf_profiler_exit(STARJ_ERR_COUNTERS_AND_GZIP);
            }
        }
    }

    if (help_request != NULL) {
        /* Help was requested */
        if (!strcmp(help_request, "events")) {
            printEvents();
            sjaf_profiler_exit(STARJ_OK);
        } else if (help_request[0] == '\0') {
            printUsage();
            sjaf_profiler_exit(STARJ_OK);
        } else {
            sjaf_error("Invalid help subject: \"%s\"", help_request);
            sjaf_profiler_exit(STARJ_ERR_UNKNOWN_OPTION);
        }
    }

    /* Optmize mode */
    if (sjav_optimize_mode) {
        sjat_field_mask mask = sjav_event_masks[STARJ_EVENT_INSTRUCTION_START];
        sjat_field_mask allowed_mask = (sjat_field_mask) (STARJ_FIELD_ENV_ID
                | STARJ_FIELD_METHOD_ID
                | STARJ_FIELD_OFFSET);
        mask = mask & ~(STARJ_COUNTED_MASK);
        if (mask != allowed_mask) {
            sjaf_warning("Optimize mode will be turned off due to requested fields");
            sjav_optimize_mode = false;
        }
    }

    sjav_agent_enabled = true;
    tmp_filename = strdup(sjav_filename);
    sjav_trace_basename = strdup(basename(tmp_filename));
    free(tmp_filename);
    tmp_filename = strdup(sjav_filename);
    sjav_trace_dirname = strdup(dirname(tmp_filename));
    free(tmp_filename);
}

/* -------------------------------------------------------------------------- *
 *                                  openFile                                  *
 *  ------------------------------------------------------------------------  *
 *                                                                            *
 *  Attemps to open the trace file.                                           *
 *                                                                            *
 *  Parameters:                                                               *
 *      + filename: the name of the file to open (and overwrite)              *
 *      + f: the address of a pointer to a FILE, which will receive the       *
 *           address of the opened FILE.                                      *
 *                                                                            *
 *  Side Effect: this function may exit the profiler if an error occurs.      *
 *                                                                            *
 * -------------------------------------------------------------------------- */
void openFile(char *filename, void **f) {
    struct stat fileStat;

    /* Sanity check */
    if (f == NULL) {
        sjaf_error("Invalid file pointer: NULL");
        sjaf_profiler_exit(STARJ_ERR_NULL_PTR);
    }
    
    if ((filename == NULL) || filename[0] == '\0') {
        /* Should not happen since the value of sjav_filename is initialized
         * to STARJ_FILENAME */
        sjaf_warning("No file name specified, using default: \"%s\"",
                STARJ_DEFAULT_FILENAME);
        if (filename == NULL) {
            filename = SJAM_NEW_ARRAY(char, strlen(STARJ_DEFAULT_FILENAME));
        }
        strcpy(filename, STARJ_DEFAULT_FILENAME);
    }

    if (stat(filename, &fileStat) >= 0) {
#ifdef STARJ_ENABLE_PIPE
        if (S_ISFIFO(fileStat.st_mode)) {
            if (!sjav_pipe_mode) {
                sjaf_warning("File \"%s\" is a FIFO. Will switch to 'pipe' mode", filename);
                sjav_pipe_mode = true;
            }
        } else if (sjav_pipe_mode) {
                sjaf_error("File \"%s\" already exists, but is not a FIFO", filename);
                sjaf_profiler_exit(STARJ_ERR_FILE_NOT_FIFO);
        } else {
#endif
            if (!S_ISREG(fileStat.st_mode)) {
                sjaf_error("File \"%s\" exists and is not a regular file", filename);
                sjaf_profiler_exit(STARJ_ERR_FILE_NOT_REG);
            }
#ifdef STARJ_ENABLE_PIPE
        }
#endif
    }

#ifdef STARJ_ENABLE_PIPE
    if (sjav_pipe_mode && (mkfifo(filename, 0600) == -1)) {
        if (errno != EEXIST) {
            sjaf_error("Pipe error: %s", strerror(errno));
            sjaf_profiler_exit(STARJ_ERR_PIPE);
        }
    }
#endif
    
#ifdef STARJ_ENABLE_PIPE
    if (sjav_pipe_mode) {
        sjaf_extra("Trying to open file in 'pipe' mode. This will only succeed once");
        sjaf_extra("    another program has opened the FIFO file for reading.");
    }
#endif
    if (sjav_gzip_output) {
        *f = gzopen(filename, "wb");
    } else {
        *f = fopen(filename, "wb");
    }
    if (*f == NULL) {
        sjaf_error("Unable to open file \"%s\"", filename);
        sjaf_profiler_exit(STARJ_ERR_FILE_OPEN);
    }
#ifdef STARJ_ENABLE_PIPE
    if (sjav_pipe_mode) {
        sjaf_extra("Another program has opened the file for reading. Execution will");
        sjaf_extra("    now resume.");
    }
#endif
}

void writeEventSpecs(FILE *f) {
    int num_field_masks;
    int i;
    int result;
    
    /* Event field masks */
    num_field_masks = 0;
    for (i = 0; i < STARJ_EVENT_COUNT; i++) {
        if (SJAM_RECORDED(sjav_event_masks[i])) {
            num_field_masks++;
        }
    }

    sjaf_write_jint(num_field_masks, f);
    for (i = 0; i < STARJ_EVENT_COUNT; i++) {
        if (SJAM_RECORDED(sjav_event_masks[i])) {
            result = sjaf_write_byte((byte) i, f);
            if (result != STARJ_OK) {
                sjaf_error("Failed to write event ID for %s event", sjav_event_names[i]);
                sjaf_profiler_exit(result);
            }
            
            result = sjaf_write_field_mask(sjav_event_masks[i], f);
            if (result != STARJ_OK) {
                sjaf_error("Failed to write mask for %s event", sjav_event_names[i]);
                sjaf_profiler_exit(result);
            }

            if (SJAM_COUNTED(sjav_event_masks[i])) {
                result = sjaf_write_jlong(sjav_event_counts[i], f);
                if (result != STARJ_OK) {
                    sjaf_error("Failed to write counter value for %s event", sjav_event_names[i]);
                    sjaf_profiler_exit(result);
                }
            }
        }
    }
}

/* -------------------------------------------------------------------------- *
 *                                writeHeader                                 *
 *  ------------------------------------------------------------------------  *
 *                                                                            *
 *  Writes the StarJ file header to the current location in an already       *
 *  opened file.                                                              *
 *                                                                            *
 *  Parameters:                                                               *
 *      + f: the file to write the header to                                  *
 *                                                                            *
 *  Side Effect: this function may exit the profiler if an error occurs.      *
 *                                                                            *
 * -------------------------------------------------------------------------- */
void writeHeader(FILE *f) {
    int result;

    /* Magic number */
    result = sjaf_write_jint(STARJ_MAGIC, f);
    if (result != STARJ_OK) {
        sjaf_error("Failed to write magic number");
        sjaf_profiler_exit(result);
    }

    /* Minor version */
    result = sjaf_write_jshort(STARJ_MINOR_VERSION, f);
    if (result != STARJ_OK) {
        sjaf_error("Failed to write minor version number");
        sjaf_profiler_exit(result);
    }

    /* Major version */
    result = sjaf_write_jshort(STARJ_MAJOR_VERSION, f);
    if (result != STARJ_OK) {
        sjaf_error("Failed to write major version number");
        sjaf_profiler_exit(result);
    }

    /* Event specifications */
    writeEventSpecs(f);

    /* Attributes */
    /* Note: so far, this feature is harcoded here */
    result = sjaf_write_jint(3, f);
    if (result != STARJ_OK) {
        sjaf_error("Failed to write attribute count");
        sjaf_profiler_exit(result);
    }

    result = sjaf_write_jint_attrib("starj.pointer.size", STARJ_PTR_SIZE, f);
    if (result != STARJ_OK) {
        sjaf_error("Failed to write pointer size attribute");
        sjaf_profiler_exit(result);
    }

    result = sjaf_write_utf8_attrib("starj.bytecode.predictor", "default", f);
    if (result != STARJ_OK) {
        sjaf_error("Failed to write bytecode predictor attribute");
        sjaf_profiler_exit(result);
    }

    result = sjaf_write_jboolean_attrib("starj.bytecode.tags",
            sjav_use_bc_tags, f);
    if (result != STARJ_OK) {
        sjaf_error("Failed to write bytecode tag attribute");
        sjaf_profiler_exit(result);
    }
}

void enableEvents(sjat_field_mask masks[], int masks_size) {
    sjat_event requiredEvents[] = {
        STARJ_EVENT_JVM_SHUT_DOWN
    };
    int numRequiredEvents = sizeof(requiredEvents);
    int i;

    /* Add dependencies */
    for (i = 0; i < numRequiredEvents; i++) {
        sjat_event e = requiredEvents[i];
        sjat_field_mask m = masks[e];
        m |= STARJ_FIELD_REQUIRED;
        masks[e] = m;
    }

    /* Enable events */
    for (i = 1; i < masks_size; i++) {
        sjat_field_mask m = masks[i];
        if ((m & STARJ_FIELD_RECORDED)
                || (m & STARJ_FIELD_COUNTED)
                || (m & STARJ_FIELD_REQUIRED)) {
            jint jvmpiEvent = sjav_event_to_jvmpi[i];

            if (jvmpiEvent & JVMPI_REQUESTED_EVENT) {
                /* JVMPI_REQUESTED_EVENT is used because there
                 * is a guarantee that it is not a valid JVMPI
                 * event ID*/
                continue;
            }
            
            if (SJAM_CALL(EnableEvent)(jvmpiEvent, NULL) != JVMPI_SUCCESS) {
                sjaf_warning("Failed to enable event: %s", sjav_event_names[i]);
                if (m & STARJ_FIELD_REQUIRED) {
                    sjaf_error("%s event is critical to the execution of the agent", sjav_event_names[i]);
                    sjaf_error("  and could not be enabled");
                    sjaf_profiler_exit(STARJ_ERR_ENABLE_EVENT);
                }
            } else {
                sjaf_extra("Enabled %s event", sjav_event_names[i]);
            }
        }
    }
}

/* -------------------------------------------------------------------------- *
 *                                  sjaf_init                                  *
 *  ------------------------------------------------------------------------  *
 *                                                                            *
 *  Performs the necessary steps to initialize the agent.                     *
 *                                                                            *
 *  Parameters:                                                               *
 *      + profiler_options: the agent's option string as provided by the      *
 *                          JVMPI interface                                   *
 *                                                                            *
 *  Side Effect: this function may exit the profiler if the provided option   *
 *               string is invalid.                                           *
 *                                                                            *
 * -------------------------------------------------------------------------- */
void sjaf_init(char *profiler_options) {
    /* Initialization */
    setOptionDefaults();
    initialize();
    processOptions(profiler_options);
    initializeModules();
    sjaf_extra("Options processed");

    /* Open file */
    openFile(sjav_filename, &sjav_file);
    sjaf_extra("File \"%s\" opened in %s mode",
            sjav_filename,
            (sjav_pipe_mode ? "pipe" : "regular"));

    /* Write header */
    writeHeader(sjav_file);
    sjaf_extra("File header successfully written");

    /* Note: up to this point, the execution was single threaded.
     *       Enabling events will soon change that. */

    /* Enable requested events */
    SJAM_START_GLOBAL_ACCESS();
    enableEvents(sjav_event_masks, STARJ_EVENT_COUNT);
    SJAM_END_GLOBAL_ACCESS();
    sjaf_extra("Events successfully enabled");

    sjaf_message("Initialization completed");
}

void sjaf_terminate() {
    int i;
    int num_counters;

    /* Cleanup code */
    
    SJAM_START_GLOBAL_ACCESS();
    
    if (!sjav_agent_enabled) {
        /* We have nothing left to do */
        SJAM_END_GLOBAL_ACCESS();
        return;
    }

    sjav_agent_enabled = false;
    
    SJAM_START_IO();

    num_counters = 0;
    for (i = 0; i < STARJ_EVENT_COUNT; i++) {
        if (sjav_event_masks[i] & STARJ_FIELD_COUNTED) {
            num_counters++;
        }
    }

    if (sjav_file != NULL) {
        SJAM_FLUSH_BUFFER(sjav_file);
        if (sjav_gzip_output) {
            gzclose(sjav_file);
        } else {
            if (num_counters > 0) {
                if (sjav_current_file_index > 0) {
                    fclose(sjav_file);
                    sjav_file = fopen(sjav_filename, "r+b");
                }
                fseek(sjav_file, 4 + 2 + 2, SEEK_SET);
                writeEventSpecs(sjav_file);
            }
            fclose(sjav_file);
        }
        sjav_file = NULL;
    }

    sjdf_hash_set_release(&sjav_known_object_ids);
    sjdf_hash_set_release(&sjav_known_method_ids);
    sjdf_hash_set_release(&sjav_known_thread_ids);
    sjdf_hash_set_release(&sjav_known_arena_ids);

    sjdf_hash_map_release(&sjav_class_id_to_methods);
    sjdf_hash_map_release(&sjav_arena_id_to_objects);
    sjdf_hash_map_release(&sjav_method_id_to_bytecode);
    
    sjdf_hash_set_release(&sjav_pending_object_ids);
    sjdf_hash_set_release(&sjav_pending_thread_ids);
    sjdf_hash_set_release(&sjav_pending_class_ids);

    sjaf_class_resolver_release();

    /* FIXME: Free all allocated memory */
    if (sjav_trace_basename != NULL) {
        free(sjav_trace_basename);
    }
    if (sjav_trace_dirname != NULL) {
        free(sjav_trace_dirname);
    }
    SJAM_END_IO();
    SJAM_END_GLOBAL_ACCESS();
}
