#ifndef _STARJ_GLOBAL_H
#define _STARJ_GLOBAL_H

/* ========================================================================== *
 *                                  Includes                                  *
 * ========================================================================== */

#include "starj_config.h" /* Import configuration */

#include <jvmpi.h>
#include <stdio.h>
#include <stdlib.h>
#include "../data/starj_data_global.h"
#include "../data/starj_hash_set.h"
#include "../data/starj_hash_map.h"

/* ========================================================================== *
 *                              Global Defaults                               *
 * ========================================================================== */

#define STARJ_DEFAULT_FILENAME "StarJ.dat"
#define STARJ_FILE_IO_MONITOR_NAME "_starj_io_monitor"
#define STARJ_GLOBAL_VAR_MONITOR_NAME "_starj_global_var_monitor"
#define STARJ_CLASS_RESOLVER_MONITOR_NAME "_starj_class_resolver_monitor"

#define STARJ_VERSION "0.1-beta8"
#define STARJ_URL "http://www.sable.mcgill.ca/starj/"
#define STARJ_AUTHOR "Bruno Dufour"
#define STARJ_AUTHOR_EMAIL "bdufou1@sable.mcgill.ca"

#define STARJ_BUFF_SIZE 1024

/* ========================================================================== *
 *                                Event Codes                                 *
 * ========================================================================== */

/* Event Values */
#define STARJ_INVALID_EVENT                       ((sjat_event) 0xFF)
#define STARJ_EVENT_ARENA_DELETE                  ((sjat_event) 0x00)
#define STARJ_EVENT_ARENA_NEW                     ((sjat_event) 0x01)
#define STARJ_EVENT_CLASS_LOAD                    ((sjat_event) 0x02)
#define STARJ_EVENT_CLASS_LOAD_HOOK               ((sjat_event) 0x03)
#define STARJ_EVENT_CLASS_UNLOAD                  ((sjat_event) 0x04)
#define STARJ_EVENT_COMPILED_METHOD_LOAD          ((sjat_event) 0x05)
#define STARJ_EVENT_COMPILED_METHOD_UNLOAD        ((sjat_event) 0x06)
#define STARJ_EVENT_DATA_DUMP_REQUEST             ((sjat_event) 0x07)
#define STARJ_EVENT_DATA_RESET_REQUEST            ((sjat_event) 0x08)
#define STARJ_EVENT_GC_FINISH                     ((sjat_event) 0x09)
#define STARJ_EVENT_GC_START                      ((sjat_event) 0x0A)
#define STARJ_EVENT_HEAP_DUMP                     ((sjat_event) 0x0B)
#define STARJ_EVENT_JNI_GLOBALREF_ALLOC           ((sjat_event) 0x0C)
#define STARJ_EVENT_JNI_GLOBALREF_FREE            ((sjat_event) 0x0D)
#define STARJ_EVENT_JNI_WEAK_GLOBALREF_ALLOC      ((sjat_event) 0x0E)
#define STARJ_EVENT_JNI_WEAK_GLOBALREF_FREE       ((sjat_event) 0x0F)
#define STARJ_EVENT_JVM_INIT_DONE                 ((sjat_event) 0x10)
#define STARJ_EVENT_JVM_SHUT_DOWN                 ((sjat_event) 0x11)
#define STARJ_EVENT_METHOD_ENTRY                  ((sjat_event) 0x12)
#define STARJ_EVENT_METHOD_ENTRY2                 ((sjat_event) 0x13)
#define STARJ_EVENT_METHOD_EXIT                   ((sjat_event) 0x14)
#define STARJ_EVENT_MONITOR_CONTENDED_ENTER       ((sjat_event) 0x15)
#define STARJ_EVENT_MONITOR_CONTENDED_ENTERED     ((sjat_event) 0x16)
#define STARJ_EVENT_MONITOR_CONTENDED_EXIT        ((sjat_event) 0x17)
#define STARJ_EVENT_MONITOR_DUMP                  ((sjat_event) 0x18)
#define STARJ_EVENT_MONITOR_WAIT                  ((sjat_event) 0x19)
#define STARJ_EVENT_MONITOR_WAITED                ((sjat_event) 0x1A)
#define STARJ_EVENT_OBJECT_ALLOC                  ((sjat_event) 0x1B)
#define STARJ_EVENT_OBJECT_DUMP                   ((sjat_event) 0x1C)
#define STARJ_EVENT_OBJECT_FREE                   ((sjat_event) 0x1D)
#define STARJ_EVENT_OBJECT_MOVE                   ((sjat_event) 0x1E)
#define STARJ_EVENT_RAW_MONITOR_CONTENDED_ENTER   ((sjat_event) 0x1F)
#define STARJ_EVENT_RAW_MONITOR_CONTENDED_ENTERED ((sjat_event) 0x20)
#define STARJ_EVENT_RAW_MONITOR_CONTENDED_EXIT    ((sjat_event) 0x21)
#define STARJ_EVENT_THREAD_END                    ((sjat_event) 0x22)
#define STARJ_EVENT_THREAD_START                  ((sjat_event) 0x23)
#define STARJ_EVENT_INSTRUCTION_START             ((sjat_event) 0x24)
/* Additional Events */
/*#define STARJ_EVENT_THREAD_STATUS_CHANGE          ((sjat_event) 0x25)*/

/* Alternative Event Representations --
 * To be converted by the event reader */
#define STARJ_EVENT_COMPACT_INSTRUCTION_START     ((sjat_event) 0xFE)

/* File Split Event */
#define STARJ_EVENT_FILESPLIT                     ((sjat_event) 0xFF)

#define STARJ_EVENT_COUNT 37

#define STARJ_REQUESTED_EVENT_MASK                ((sjat_event) 0x80)

/* ========================================================================== *
 *                                Error Codes                                 *
 * ========================================================================== */

#define STARJ_OK                         ((jint)   0)
#define STARJ_ERR_UNKNOWN_OPTION         ((jint)  -1)
#define STARJ_ERR_UNKNOWN_OPTION_KIND    ((jint)  -2)
#define STARJ_ERR_INVALID_BOOL           ((jint)  -3)
#define STARJ_ERR_INVALID_INT            ((jint)  -4)
#define STARJ_ERR_INVALID_SIZE           ((jint)  -5)
#define STARJ_ERR_INDEX_OUT_OF_BOUNDS    ((jint)  -6)
#define STARJ_ERR_INVALID_VERBOSITY_CHAR ((jint)  -7)
#define STARJ_ERR_INVALID_EVENT_CHAR     ((jint)  -8)
#define STARJ_ERR_FILE_NOT_FIFO          ((jint)  -9)
#define STARJ_ERR_FILE_NOT_REG           ((jint) -10)
#define STARJ_ERR_PIPE                   ((jint) -11)
#define STARJ_ERR_FILE_OPEN              ((jint) -12)
#define STARJ_ERR_NULL_PTR               ((jint) -13)
#define STARJ_ERR_IO                     ((jint) -14)
#define STARJ_ERR_SPEC_MAGIC             ((jint) -15)
#define STARJ_ERR_SPEC_VERSION           ((jint) -16)
#define STARJ_ERR_SPEC_COUNT             ((jint) -17)
#define STARJ_ERR_ENABLE_EVENT           ((jint) -18)
#define STARJ_ERR_COUNTERS_AND_PIPE      ((jint) -19)
#define STARJ_ERR_COUNTERS_AND_GZIP      ((jint) -20)
#define STARJ_ERR_ZIP_GLOBAL_INFO        ((jint) -21)
#define STARJ_ERR_ZIP_FILE_INFO          ((jint) -22)
#define STARJ_ERR_ZIP_FILE_POS           ((jint) -23)
#define STARJ_ERR_ZIP_NEXT_FILE          ((jint) -24)
#define STARJ_ERR_NOT_FOUND              ((jint) -25)
#define STARJ_ERR_MALLOC                 ((jint) -26)
#define STARJ_ERR_STAT                   ((jint) -27)
#define STARJ_ERR_RECORD_KIND            ((jint) -28)
#define STARJ_ERR_CLASS_PARSE            ((jint) -29)
#define STARJ_ERR_CLASS_NAME             ((jint) -30)
#define STARJ_ERR_SET_INIT               ((jint) -31)

#define STARJ_ERR_NOT_IMPL               ((jint) -99)

/* ========================================================================== *
 *                              Verbosity Levels                              *
 * ========================================================================== */

#define STARJ_VERBOSE_ERR     0x01
#define STARJ_VERBOSE_WARN    0x02
#define STARJ_VERBOSE_MESS    0x04
#define STARJ_VERBOSE_DEBUG   0x08
#define STARJ_VERBOSE_EXTRA   0x10

#define STARJ_VERBOSITY_NONE  (0x00)
#define STARJ_VERBOSITY_ERR   (STARJ_VERBOSE_ERR)
#define STARJ_VERBOSITY_WARN  (STARJ_VERBOSITY_ERR | STARJ_VERBOSE_WARN)
#define STARJ_VERBOSITY_MESS  (STARJ_VERBOSITY_WARN | STARJ_VERBOSE_MESS)
#define STARJ_VERBOSITY_DEBUG (STARJ_VERBOSITY_MESS | STARJ_VERBOSE_DEBUG)
#define STARJ_VERBOSITY_HIGH  (STARJ_VERBOSITY_MESS | STARJ_VERBOSE_EXTRA)
#define STARJ_VERBOSITY_ALL   (0xFF)

#define STARJ_VERBOSITY_DEFAULT STARJ_VERBOSITY_MESS
#define STARJ_VERBOSITY_NORMAL STARJ_VERBOSITY_MESS
#define STARJ_VERBOSITY_LOW STARJ_VERBOSITY_WARN


/* ========================================================================== *
 *                                   Types                                    *
 * ========================================================================== */

typedef enum {false, true} bool;
typedef unsigned char byte;

typedef jint sjat_field_mask;
typedef byte sjat_event;

/* JVMPI Types */
typedef JNIEnv * sjat_thread_id;
typedef jobjectID sjat_object_id;
typedef jobjectID sjat_class_id;
typedef jmethodID sjat_method_id;
typedef jint sjat_arena_id;
typedef jobject sjat_jni_ref_id;
typedef JVMPI_RawMonitor sjat_raw_monitor_id;

#define JSHORT_MIN ((jshort) (1 << (sizeof(jshort) * 8 - 1)))
#define JSHORT_MAX ((jshort)(~JSHORT_MIN))

#define JINT_MIN ((jint) (1 << (sizeof(jint) * 8 - 1)))
#define JINT_MAX ((jint)(~JINT_MIN))

/* ========================================================================== *
 *                                 Variables                                  *
 * ========================================================================== */

/* VM Structures */
extern JavaVM *sjav_jvm;
extern JVMPI_Interface *sjav_jvmpi_interface;
extern JVMPI_RawMonitor sjav_file_io_lock;
extern JVMPI_RawMonitor sjav_global_var_lock;
extern JVMPI_RawMonitor sjav_class_resolver_lock;

/* Global variables */
extern void *sjav_file;
extern bool sjav_agent_enabled;
extern char sjav_event_names[STARJ_EVENT_COUNT][30];
extern jint sjav_event_to_jvmpi[];
extern sjat_event *sjav_jvmpi_to_event;
extern jlong sjav_total_executed_insts;
extern jint sjav_current_file_size;
extern jint sjav_current_file_index;
extern jlong sjav_event_counts[STARJ_EVENT_COUNT];

/* Maps & sets */
extern sjdt_hash_set sjav_known_object_ids;
extern sjdt_hash_set sjav_known_method_ids;
extern sjdt_hash_set sjav_known_thread_ids;
extern sjdt_hash_set sjav_known_arena_ids;
extern sjdt_hash_map sjav_class_id_to_methods;
extern sjdt_hash_map sjav_arena_id_to_objects;
extern sjdt_hash_map sjav_method_id_to_bytecode;

extern sjdt_hash_set sjav_pending_object_ids;
extern sjdt_hash_set sjav_pending_thread_ids;
extern sjdt_hash_set sjav_pending_class_ids;


/* Options */
extern char *sjav_filename;
extern char *sjav_trace_basename;
extern char *sjav_trace_dirname;
extern char *sjav_spec_filename;
extern sjat_field_mask sjav_event_masks[];
extern int sjav_split_threshold;
extern bool sjav_pipe_mode;
extern int sjav_verbosity_level;
extern bool sjav_optimize_mode;
extern char **sjav_class_path;
extern int sjav_class_path_len;
extern bool sjav_colours;
extern bool sjav_use_bc_tags;
extern bool sjav_gzip_output;
extern bool sjav_unique_ids;
extern FILE *sjav_out_stream;

/* Debugging support */
#ifdef STARJ_DBG_TRACE_EVENTS
    extern FILE *sjav_dbg_event_file;
#endif
#ifdef STARJ_DBG_TRACE_REQUESTS
    extern FILE *sjav_dbg_req_file;
#endif

/* ========================================================================== *
 *                                   Macros                                   *
 * ========================================================================== */

#define SJAM_CALL(f) (sjav_jvmpi_interface->f)
#define SJAM_NEW(type) (type *) malloc(sizeof(type))
#define SJAM_NEW_ARRAY(type, size) (type *) calloc(size, sizeof(type))

#ifdef STARJ_LOCK_IO
    #define SJAM_START_IO() SJAM_CALL(RawMonitorEnter)(sjav_file_io_lock)
    #define SJAM_END_IO() SJAM_CALL(RawMonitorExit)(sjav_file_io_lock)
#else
    #define SJAM_START_IO()
    #define SJAM_END_IO()
#endif

#ifdef STARJ_USE_INLINE
    #define SJAM_INLINE inline
#else
    #define SJAM_INLINE
#endif

#ifdef STARJ_LOCK_GLOBAL_VARS
    #define SJAM_START_GLOBAL_ACCESS() SJAM_CALL(RawMonitorEnter)(sjav_global_var_lock)
    #define SJAM_END_GLOBAL_ACCESS() SJAM_CALL(RawMonitorExit)(sjav_global_var_lock)
#else
    #define SJAM_START_GLOBAL_ACCESS()
    #define SJAM_END_GLOBAL_ACCESS()
#endif

#ifdef STARJ_LOCK_CLASS_RESOLVER
    #define SJAM_START_CLASS_RESOLVER_ACCESS() SJAM_CALL(RawMonitorEnter)(sjav_class_resolver_lock)
    #define SJAM_END_CLASS_RESOLVER_ACCESS() SJAM_CALL(RawMonitorExit)(sjav_class_resolver_lock)
#else
    #define SJAM_START_CLASS_RESOLVER_ACCESS()
    #define SJAM_END_CLASS_RESOLVER_ACCESS()
#endif

/* Check the pointer size (default is 4) */
#ifdef STARJ_PTR_SIZE
    #if (STARJ_PTR_SIZE != 1) && (STARJ_PTR_SIZE != 2) && (STARJ_PTR_SIZE != 4) && (STARJ_PTR_SIZE != 8)
        #error Invalid pointer size (use 1, 2, 4, or 8)
    #endif
#else
    #define STARJ_PTR_SIZE 4
#endif

#ifdef STARJ_ENABLE_BC_TAGGING
    #define SJAM_BC_TAG(file) \
        if (sjav_use_bc_tags) {\
            SJAM_WRITE_JLONG(sjav_total_executed_insts, file);\
        }
#else
    #define SJAM_GC_TAG(file) 
#endif

#endif
