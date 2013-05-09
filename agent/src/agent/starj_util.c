#include "starj_util.h"
#include "starj_io.h"
#include "starj_setup.h"

#include <string.h>
#include <stdlib.h>
#include <zlib.h>

void sjaf_profiler_exit(jint return_val) {
    sjaf_terminate();

    switch (return_val) {
        case STARJ_OK:
            break;
        case STARJ_ERR_NOT_IMPL:
            sjaf_message("Execution terminated because some part of the agent");
            sjaf_message("    has not yet been implemented. Visit");
            sjaf_message("    %s", STARJ_URL);
            sjaf_message("    to get the latest version");
            break;
        default:
            sjaf_error("Execution terminated abnormally");
            break;
    }
    
    SJAM_CALL(ProfilerExit)(return_val);
}

void sjaf_split_file() {
    size_t len; 
    char *next_filename;

    SJAM_START_GLOBAL_ACCESS();
    SJAM_START_IO();

    len = (sjav_trace_dirname != NULL ? strlen(sjav_trace_dirname) : 0)
            + strlen(sjav_trace_basename) + 7; // dir + / + basename + _XXXX\0
        
    sjav_current_file_index++;

    SJAM_FLUSH_BUFFER(sjav_file);
    // Important: non-buffered writes until we return from this function!
    // (i.e. don't use the SJAM_* form)
    sjaf_write_event(STARJ_EVENT_FILESPLIT, sjav_file);
    
    next_filename = SJAM_NEW_ARRAY(char, len);
    if (next_filename == NULL) {
        sjaf_error("Failed to allocate space for new file name");
        sjaf_profiler_exit(STARJ_ERR_MALLOC);
    }

    sprintf(next_filename, "%s_%04d", sjav_trace_basename,
            sjav_current_file_index);
    sjaf_write_utf8(next_filename, sjav_file); 
    sprintf(next_filename, "%s/%s_%04d",
            (sjav_trace_dirname != NULL ? sjav_trace_dirname : ""),
            sjav_trace_basename, sjav_current_file_index);
    if (sjav_gzip_output) {
        gzclose(sjav_file);
        sjav_file = gzopen(next_filename, "wb");
    } else {
        fclose(sjav_file);
        sjav_file = fopen(next_filename, "wb");
        if (sjav_file == NULL) {
            sjaf_error("Failed to open new trace file: %s\n", next_filename);
            free(next_filename);
            sjaf_profiler_exit(STARJ_ERR_FILE_OPEN);
        }
    }
    sjaf_debug("Threshold reached. Starting new file: \"%s\"\n", next_filename);

    sjav_current_file_size = 0;
    free(next_filename);

    SJAM_END_IO();
    SJAM_END_GLOBAL_ACCESS();
}

int sjaf_parse_bool(char *buff, bool *result) {
    if ((buff == NULL) || (*buff == '\0')) {
        return 1;
    }
    
    if (!strcmp(buff, "true") || !strcmp(buff, "on") || !strcmp(buff, "yes")) {
        *result = true;
        return 1;
    } else if (!strcmp(buff, "false") || !strcmp(buff, "off") || !strcmp(buff, "no")) {
        *result = false;
        return 1;
    } 

    return 0;
}

int sjaf_parse_int(char *buff, int *result) {
    char **p;
    long int i;
    
    if ((buff == NULL) || (*buff == '\0')) {
        return 1;
    }
    
    p = &buff;
    i = strtol(buff, p, 10);
    if (*p == '\0') {
        *result = ((int) i);
        return 1;
    }

    return 0;
}

int sjaf_parse_size(char *buff, int *result) {
    char **p;
    long int i;
    
    if ((buff == NULL) || (*buff == '\0')) {
        return 1;
    }
    
    p = &buff;
    i = strtol(buff, p, 10);
    
    if (*p == '\0') {
        // Bytes
        *result = ((int) i);
    } else if (!strcmp(*p, "k") || !strcmp(*p, "K")) {
        // Kilobytes
        *result = i * 1024;
    } else if (!strcmp(*p, "m") || !strcmp(*p, "M")) {
        // Megabytes
        *result = i * 1024 * 1024;
    } else if (!strcmp(*p, "g") || !strcmp(*p, "G")) {
        // Gigabytes
        *result = i * 1024 * 1024 * 1024;
    } else {
        sjaf_error("Unrecognized size modifier: \"%s\"", *p);
        return 0;
    }

    return 1;
}

int sjaf_starts_with(const char *str, const char *prefix) {
    int prefix_len, str_len;
    if ((prefix == NULL) || (str == NULL)) {
        return 0;
    }

    prefix_len = strlen(prefix);
    if (prefix_len == 0) {
        return 1; /* Java API compliance */
    }
    str_len = strlen(str);

    if (prefix_len > str_len) {
        return 0;
    }

    return (strncmp(str, prefix, prefix_len) == 0);
}


int sjaf_ends_with(const char *str, const char *suffix) {
    int suffix_len, str_len;
    
    if ((suffix == NULL) || (str == NULL)) {
        return 0;
    }

    suffix_len = strlen(suffix);
    if (suffix_len == 0) {
        return 1; // Java API compliance
    }
    str_len = strlen(str);

    if (suffix_len > str_len) {
        return 0;
    }

    return (strcmp(str + (str_len - suffix_len), suffix) == 0);
}


int sjaf_is_char_at(const char *str, unsigned int pos, char c) {
    if ((str == NULL) || (pos >= strlen(str))) {
        return 0;
    }
    
    return (str[pos] == c);
}

int sjaf_replace(char *str, char from, char to) {
    if (str == NULL) {
        return STARJ_ERR_NULL_PTR;
    }
    
    for (; *str; str++) {
        if (*str == from) {
            *str = to;
        }
    }

    return STARJ_OK;
}
