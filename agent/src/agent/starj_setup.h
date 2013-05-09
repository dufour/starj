#ifndef _STARJ_SETUP_H
#define _STARJ_SETUP_H

#include "starj_global.h"
#include "starj_io.h"

/* Magic number */
/* Note: The magic number spells out Sable SEF,
 * for Sable StarJ Event File */
#define STARJ_MAGIC         ((jint) 0x5AB1E5EF)
#define STARJ_MAJOR_VERSION ((jshort) 0x0001)
#define STARJ_MINOR_VERSION ((jshort) 0x0000)

/* Agent options */
#define STARJ_AGENT_PIPE_MODE      ((jint) 0x000000001)
#define STARJ_AGENT_UNIQUE_ID_MODE ((jint) 0x000000002)
#define STARJ_AGENT_BC_TAG_MODE    ((jint) 0x000000004)

/* Option Types */
#define STARJ_OPT_TYPE_BOOL       0
#define STARJ_OPT_TYPE_INT        1
#define STARJ_OPT_TYPE_STRING     2
#define STARJ_OPT_TYPE_CLASSPATH  3
#define STARJ_OPT_TYPE_SIZE       4
#define STARJ_OPT_TYPE_VERBOSITY  5
#define STARJ_OPT_TYPE_EVENT_MASK 6

/* Option indexes */
#define STARJ_OPT_FILE        0
#define STARJ_OPT_SPECFILE    1
#define STARJ_OPT_EVENTS      2
#define STARJ_OPT_COUNTERS    3
#define STARJ_OPT_SPLIT       4
#define STARJ_OPT_PIPE        5
#define STARJ_OPT_VERBOSE     6
#define STARJ_OPT_OPTIMIZE    7
#define STARJ_OPT_CP          8
#define STARJ_OPT_CP_APPEND   9
#define STARJ_OPT_CP_PREPEND 10
#define STARJ_OPT_COLOURS    11
#define STARJ_OPT_BC_TAGS    12
#define STARJ_OPT_GZIP       13
#define STARJ_OPT_UNIQUE_IDS 14
#define STARJ_OPT_HELP       15

#define STARJ_OPT_COUNT (STARJ_OPT_HELP + 1)

/* Class path placement options */
typedef enum {
    NO_ENV_CP,
    ENV_CP_PREPEND, 
    ENV_CP_APPEND
} sjat_env_classpath_option;

/* Function prototypes */
void sjaf_init(char *profiler_options);
void sjaf_terminate();

#endif
