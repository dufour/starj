#include "starj_agent.h"
#include "starj_util.h"
#include "starj_spec.h"
#include "starj_id_resolver.h"
#include "../data/starj_hash_set.h"
#include "../data/starj_hash_map.h"

/* ========================================================================== *
 *                              Global Variables                              *
 * ========================================================================== */

JVMPI_Interface *sjav_jvmpi_interface;
JavaVM *sjav_jvm;
JVMPI_RawMonitor sjav_file_io_lock;
JVMPI_RawMonitor sjav_global_var_lock;
JVMPI_RawMonitor sjav_class_resolver_lock;

bool sjav_agent_enabled = false;
void *sjav_file = NULL;
jlong sjav_total_executed_insts = 0;
jint sjav_current_file_size = 0;
jint sjav_current_file_index = 0;
jlong sjav_event_counts[STARJ_EVENT_COUNT];

/* Options */
char *sjav_filename;
char *sjav_trace_basename = NULL;
char *sjav_trace_dirname = NULL;
char *sjav_spec_filename;
sjat_field_mask sjav_event_masks[STARJ_EVENT_COUNT];
int sjav_split_threshold;
bool sjav_pipe_mode;
int sjav_verbosity_level = STARJ_VERBOSITY_DEFAULT;
bool sjav_optimize_mode;
char **sjav_class_path;
int sjav_class_path_len;
bool sjav_colours;
bool sjav_use_bc_tags;
bool sjav_gzip_output;
bool sjav_unique_ids;
FILE *sjav_out_stream;

/* Debugging support */
#ifdef STARJ_DBG_TRACE_EVENTS
    FILE *sjav_dbg_event_file;
#endif
#ifdef STARJ_DBG_TRACE_REQUESTS
    FILE *sjav_dbg_req_file;
#endif


/* Maps & sets */
sjdt_hash_set sjav_known_object_ids;
sjdt_hash_set sjav_known_method_ids;
sjdt_hash_set sjav_known_thread_ids;
sjdt_hash_set sjav_known_arena_ids;
sjdt_hash_map sjav_class_id_to_methods;
sjdt_hash_map sjav_arena_id_to_objects;
sjdt_hash_map sjav_method_id_to_bytecode;

sjdt_hash_set sjav_pending_object_ids;
sjdt_hash_set sjav_pending_thread_ids;
sjdt_hash_set sjav_pending_class_ids;

char sjav_event_names[STARJ_EVENT_COUNT][30] = {
    "ARENA_DELETE",
    "ARENA_NEW",
    "CLASS_LOAD",
    "CLASS_LOAD_HOOK",
    "CLASS_UNLOAD",
    "COMPILED_METHOD_LOAD",
    "COMPILED_METHOD_UNLOAD",
    "DATA_DUMP_REQUEST",
    "DATA_RESET_REQUEST",
    "GC_FINISH",
    "GC_START",
    "HEAP_DUMP",
    "JNI_GLOBALREF_ALLOC",
    "JNI_GLOBALREF_FREE",
    "JNI_WEAK_GLOBALREF_ALLOC",
    "JNI_WEAK_GLOBALREF_FREE",
    "JVM_INIT_DONE",
    "JVM_SHUT_DOWN",
    "METHOD_ENTRY",
    "METHOD_ENTRY2",
    "METHOD_EXIT",
    "MONITOR_CONTENDED_ENTER",
    "MONITOR_CONTENDED_ENTERED",
    "MONITOR_CONTENDED_EXIT",
    "MONITOR_DUMP",
    "MONITOR_WAIT",
    "MONITOR_WAITED",
    "OBJECT_ALLOC",
    "OBJECT_DUMP",
    "OBJECT_FREE",
    "OBJECT_MOVE",
    "RAW_MONITOR_CONTENDED_ENTER",
    "RAW_MONITOR_CONTENDED_ENTERED",
    "RAW_MONITOR_CONTENDED_EXIT",
    "THREAD_END",
    "THREAD_START",
    "INSTRUCTION_START"
    /*"THREAD_STATUS_CHANGE"*/
};

jint sjav_event_to_jvmpi[STARJ_EVENT_COUNT] = {
    JVMPI_EVENT_ARENA_DELETE,
    JVMPI_EVENT_ARENA_NEW,
    JVMPI_EVENT_CLASS_LOAD,
    JVMPI_EVENT_CLASS_LOAD_HOOK,
    JVMPI_EVENT_CLASS_UNLOAD,
    JVMPI_EVENT_COMPILED_METHOD_LOAD,
    JVMPI_EVENT_COMPILED_METHOD_UNLOAD,
    JVMPI_EVENT_DATA_DUMP_REQUEST,
    JVMPI_EVENT_DATA_RESET_REQUEST,
    JVMPI_EVENT_GC_FINISH,
    JVMPI_EVENT_GC_START,
    JVMPI_EVENT_HEAP_DUMP,
    JVMPI_EVENT_JNI_GLOBALREF_ALLOC,
    JVMPI_EVENT_JNI_GLOBALREF_FREE,
    JVMPI_EVENT_JNI_WEAK_GLOBALREF_ALLOC,
    JVMPI_EVENT_JNI_WEAK_GLOBALREF_FREE,
    JVMPI_EVENT_JVM_INIT_DONE,
    JVMPI_EVENT_JVM_SHUT_DOWN,
    JVMPI_EVENT_METHOD_ENTRY,
    JVMPI_EVENT_METHOD_ENTRY2,
    JVMPI_EVENT_METHOD_EXIT,
    JVMPI_EVENT_MONITOR_CONTENDED_ENTER,
    JVMPI_EVENT_MONITOR_CONTENDED_ENTERED,
    JVMPI_EVENT_MONITOR_CONTENDED_EXIT,
    JVMPI_EVENT_MONITOR_DUMP,
    JVMPI_EVENT_MONITOR_WAIT,
    JVMPI_EVENT_MONITOR_WAITED,
    JVMPI_EVENT_OBJECT_ALLOC,
    JVMPI_EVENT_OBJECT_DUMP,
    JVMPI_EVENT_OBJECT_FREE,
    JVMPI_EVENT_OBJECT_MOVE,
    JVMPI_EVENT_RAW_MONITOR_CONTENDED_ENTER,
    JVMPI_EVENT_RAW_MONITOR_CONTENDED_ENTERED,
    JVMPI_EVENT_RAW_MONITOR_CONTENDED_EXIT,
    JVMPI_EVENT_THREAD_END,
    JVMPI_EVENT_THREAD_START,
    JVMPI_EVENT_INSTRUCTION_START
    /*JVMPI_REQUESTED_EVENT*/
};

sjat_event *sjav_jvmpi_to_event;

/* ========================================================================== *
 *                               Implementation                               *
 * ========================================================================== */

void sjaf_notifyEvent(JVMPI_Event *event) {
    /* Get Event Type (clear REQUESTED_EVENT bit) */
    jint jvmpi_event = event->event_type & ~JVMPI_REQUESTED_EVENT;
    bool requested = (event->event_type & JVMPI_REQUESTED_EVENT ? true : false);
    sjat_event e = sjav_jvmpi_to_event[jvmpi_event];
    /* sjat_field_mask mask = sjav_event_masks[e]; */

#ifdef STARJ_DBG_TRACE_EVENTS
    fprintf(sjav_dbg_event_file, "Received event %s\n", sjav_event_names[e]);
#endif
    
    /* Sanity check */
    switch (jvmpi_event) {
        case JVMPI_EVENT_RAW_MONITOR_CONTENDED_ENTER:
        case JVMPI_EVENT_RAW_MONITOR_CONTENDED_ENTERED:
        case JVMPI_EVENT_RAW_MONITOR_CONTENDED_EXIT: {
                const char *rm_name = event->u.raw_monitor.name;
                if ((rm_name != NULL) && (rm_name[0] == '_')) {
                    /* This is a Raw Monitor event that we should not receive.
                     * This may seem like a redundant check, but incorrect
                     * behaviour has been observed on at least one benchmark */
                    return;
                }
            }
            break;
        case STARJ_INVALID_EVENT:
            return;
        default:
            break;
    }

    SJAM_START_GLOBAL_ACCESS();
    if (!sjav_agent_enabled) {
        SJAM_END_GLOBAL_ACCESS();
        return;
    }
    sjav_event_counts[e] += 1;
    SJAM_END_GLOBAL_ACCESS();

    if (!(sjav_event_masks[e] & (STARJ_FIELD_RECORDED | STARJ_FIELD_REQUIRED))) {
        return;
    }
    
    if (jvmpi_event != JVMPI_EVENT_INSTRUCTION_START) {
        sjaf_clear_pending_ise();
    }

    switch (jvmpi_event) {
        case JVMPI_EVENT_ARENA_DELETE:
            sjaf_handler_arena_delete(event->env_id,
                    event->u.delete_arena.arena_id,
                    requested);
            if (sjav_unique_ids) {
                sjaf_handle_arena_delete(event->u.delete_arena.arena_id, requested);
            }
            break;
        case JVMPI_EVENT_ARENA_NEW:
            if (sjav_unique_ids) {
                sjaf_handle_arena_new(event->u.new_arena.arena_id, requested);
            }
            sjaf_handler_arena_new(event->env_id,
                    event->u.new_arena.arena_id,
                    event->u.new_arena.arena_name,
                    requested);
            break;
        case JVMPI_EVENT_CLASS_LOAD:
            sjaf_handler_class_load(event->env_id,
                    event->u.class_load.class_name,
                    event->u.class_load.source_name,
                    event->u.class_load.num_interfaces,
                    event->u.class_load.num_methods,
                    event->u.class_load.methods,
                    event->u.class_load.num_static_fields,
                    event->u.class_load.statics,
                    event->u.class_load.num_instance_fields,
                    event->u.class_load.instances,
                    event->u.class_load.class_id,
                    requested);
            break;
        case JVMPI_EVENT_CLASS_LOAD_HOOK:
            event->u.class_load_hook.new_class_data
                    = event->u.class_load_hook.class_data;
            event->u.class_load_hook.new_class_data_len
                    = event->u.class_load_hook.class_data_len;
            sjaf_handler_class_load_hook(event->u.class_load_hook.class_data,
                    event->u.class_load_hook.class_data_len,
                    &event->u.class_load_hook.new_class_data,
                    &event->u.class_load_hook.new_class_data_len,
                    event->u.class_load_hook.malloc_f,
                    requested);
            break;
        case JVMPI_EVENT_CLASS_UNLOAD:
            sjaf_handler_class_unload(event->env_id,
                    event->u.class_unload.class_id,
                    requested);
            break;
        case JVMPI_EVENT_GC_FINISH:
            sjaf_handler_gc_finish(event->env_id,
                    event->u.gc_info.used_objects,
                    event->u.gc_info.used_object_space,
                    event->u.gc_info.total_object_space,
                    requested);
            break;
        case JVMPI_EVENT_GC_START:
            sjaf_handler_gc_start(event->env_id, requested);
            break;
        case JVMPI_EVENT_INSTRUCTION_START:
            sjaf_handler_instruction_start(event->env_id,
                    event->u.instruction.method_id,
                    event->u.instruction.offset,
                    event->u.instruction.u.if_info.is_true,
                    event->u.instruction.u.tableswitch_info.key,
                    event->u.instruction.u.tableswitch_info.low,
                    event->u.instruction.u.tableswitch_info.hi,
                    event->u.instruction.u.lookupswitch_info.chosen_pair_index,
                    event->u.instruction.u.lookupswitch_info.pairs_total,
                    requested);
            break;
        case JVMPI_EVENT_JVM_INIT_DONE:
            sjaf_handler_jvm_init_done(event->env_id, requested);
            break;
        case JVMPI_EVENT_JVM_SHUT_DOWN:
            sjaf_handler_jvm_shut_down(event->env_id, requested);
            break;
        case JVMPI_EVENT_METHOD_ENTRY:
            sjaf_handler_method_entry(event->env_id,
                    event->u.method.method_id,
                    requested);
            break;
        case JVMPI_EVENT_METHOD_ENTRY2:
            sjaf_handler_method_entry2(event->env_id,
                    event->u.method_entry2.method_id,
                    event->u.method_entry2.obj_id,
                    requested);
            break;
        case JVMPI_EVENT_METHOD_EXIT:
            sjaf_handler_method_exit(event->env_id,
                    event->u.method.method_id,
                    requested);
            break;
        case JVMPI_EVENT_MONITOR_CONTENDED_ENTER:
            sjaf_handler_monitor_contended_enter(event->env_id,
                    event->u.monitor.object,
                    requested);
            break;
        case JVMPI_EVENT_MONITOR_CONTENDED_ENTERED:
            sjaf_handler_monitor_contended_entered(event->env_id,
                    event->u.monitor.object,
                    requested);
            break;
        case JVMPI_EVENT_MONITOR_CONTENDED_EXIT:
            sjaf_handler_monitor_contended_exit(event->env_id,
                    event->u.monitor.object,
                    requested);
            break;
        case JVMPI_EVENT_MONITOR_WAIT:
            sjaf_handler_monitor_wait(event->env_id,
                    event->u.monitor_wait.object,
                    event->u.monitor_wait.timeout,
                    requested);
            break;
        case JVMPI_EVENT_MONITOR_WAITED:
            sjaf_handler_monitor_waited(event->env_id,
                    event->u.monitor_wait.object,
                    event->u.monitor_wait.timeout,
                    requested);
            break;
        case JVMPI_EVENT_OBJECT_ALLOC:
            if (sjav_unique_ids) {
                sjaf_handle_object_alloc(event->u.obj_alloc.obj_id, requested);
            }
            sjaf_handler_object_alloc(event->env_id,
                    event->u.obj_alloc.arena_id,
                    event->u.obj_alloc.class_id,
                    event->u.obj_alloc.is_array,
                    event->u.obj_alloc.size,
                    event->u.obj_alloc.obj_id,
                    requested);
            break;
        case JVMPI_EVENT_OBJECT_FREE:
            sjaf_handler_object_free(event->env_id,
                    event->u.obj_free.obj_id,
                    requested);
            if (sjav_unique_ids) {
                sjaf_handle_object_free(event->u.obj_free.obj_id, requested);
            }
            break;
        case JVMPI_EVENT_OBJECT_MOVE:
            sjaf_handler_object_move(event->env_id,
                    event->u.obj_move.arena_id,
                    event->u.obj_move.obj_id,
                    event->u.obj_move.new_arena_id,
                    event->u.obj_move.new_obj_id,
                    requested);
            if (sjav_unique_ids) {
                sjaf_handle_object_move(event->u.obj_move.obj_id, event->u.obj_move.new_obj_id, requested);
            }
            break;
        case JVMPI_EVENT_RAW_MONITOR_CONTENDED_ENTER:
            sjaf_handler_raw_monitor_contended_enter(event->env_id,
                    event->u.raw_monitor.name,
                    event->u.raw_monitor.id,
                    requested);
            break;
        case JVMPI_EVENT_RAW_MONITOR_CONTENDED_ENTERED:
            sjaf_handler_raw_monitor_contended_entered(event->env_id,
                    event->u.raw_monitor.name,
                    event->u.raw_monitor.id,
                    requested);
            break;
        case JVMPI_EVENT_RAW_MONITOR_CONTENDED_EXIT:
            sjaf_handler_raw_monitor_contended_exit(event->env_id,
                    event->u.raw_monitor.name,
                    event->u.raw_monitor.id,
                    requested);
            break;
        case JVMPI_EVENT_THREAD_END:
            sjaf_handler_thread_end(event->env_id, requested);
            if (sjav_unique_ids) {
                sjaf_handle_thread_end(event->env_id, requested);
            }
            break;
        case JVMPI_EVENT_THREAD_START:
            if (sjav_unique_ids) {
                sjaf_handle_thread_start(event->u.thread_start.thread_env_id, requested);
            }
            sjaf_handler_thread_start(event->env_id,
                    event->u.thread_start.thread_name,
                    event->u.thread_start.group_name,
                    event->u.thread_start.parent_name,
                    event->u.thread_start.thread_id,
                    event->u.thread_start.thread_env_id,
                    requested);
            break;
        default:
            /* sjaf_debug("Event %s not implemented", sjav_event_names[e]); */
            break;
    }

    if (jvmpi_event == JVMPI_EVENT_JVM_SHUT_DOWN) {
        sjaf_terminate();
        sjaf_message("Completed program execution");
    }

    if ((sjav_split_threshold > 0) && (sjav_current_file_size >= sjav_split_threshold)) {
        sjaf_split_file();
    }
}

JNIEXPORT jint JNICALL JVM_OnLoad(JavaVM *vm, char *options, void *reserved) {
    /* Obtain the JVMPI interface */
    sjav_jvm = vm;

    if (((*vm)->GetEnv(vm, (void **)&sjav_jvmpi_interface, JVMPI_VERSION_1)) < 0) {
        return JNI_ERR;
    }

    /* 'notifyEvent' will be called for each event */
    sjav_jvmpi_interface->NotifyEvent = sjaf_notifyEvent;

    /* Create the locks which prevents data corruption in
     * multithreaded programs */
    sjav_file_io_lock = SJAM_CALL(RawMonitorCreate)(STARJ_FILE_IO_MONITOR_NAME);
    sjav_global_var_lock = SJAM_CALL(RawMonitorCreate)(STARJ_GLOBAL_VAR_MONITOR_NAME);
    sjav_class_resolver_lock = SJAM_CALL(RawMonitorCreate)(STARJ_CLASS_RESOLVER_MONITOR_NAME);

    /* Initialize the agent */
    sjaf_init(options);

    return JNI_OK;
}
