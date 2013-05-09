#include "starj_class_events.h"
#include "starj_io.h"
#include "starj_spec.h"
#include "starj_class_resolver.h"
#include "../cfreader/cfreader.h"
#include "../cfreader/cfreader_bytecode.h"

int process_class(const char *name, jint num_methods, JVMPI_Method *methods,
        sjat_object_id class_id) {
    cfrt_classfile class_file;
    sjdt_hash_set *method_set;
    bool class_avail;
    int result;
    jint i;
    
    if (name == NULL) {
        sjaf_debug("NULL class name");
        return STARJ_ERR_NULL_PTR;
    }
    
    method_set = SJAM_NEW(sjdt_hash_set);
    if (method_set == NULL) {
        sjaf_debug("Failed to allocate new set");
        return STARJ_ERR_MALLOC;
    }

    if (sjdf_hash_set_init(method_set, num_methods, 1.0, NULL, NULL, NULL, NULL) != STARJ_DATA_OK) {
        sjaf_debug("Failed to initialize set");
        return STARJ_ERR_SET_INIT;
    }

    if (sjdf_hash_map_put(&sjav_class_id_to_methods, class_id, method_set) != STARJ_DATA_OK) {
        sjaf_error("Failed to add method set to class ID map");
        free(method_set);
        method_set = NULL;
    }
    
    /* Try to locate the class */
    class_avail = false;
    if (sjav_optimize_mode) {
        if (name[0] != '[') { /* Check whether the class is a regular one */
            result = sjaf_resolve_class(name, &class_file);
            if (result == STARJ_OK) {
                class_avail = true;
            } else {
                sjaf_warning("Failed to resolve location of class \"%s\"", name);
            }
        }
    }

    /* Process each method */
    for (i = 0; i < num_methods; i++) {
        sjat_method_id mid = methods[i].method_id;

        sjdf_hash_set_add(&sjav_known_method_ids, mid);
        if (method_set != NULL) {
#ifdef STARJ_DBG_TRACE_REQUESTS
            fprintf(sjav_dbg_req_file, " Registering method %d from class %d\n",
                    mid, class_id);
#endif
            sjdf_hash_set_add(method_set, mid);
        }

        if (class_avail) {
            cfrt_bytecode *bytecode;
            cfrt_method_info *minfo;
         
            /* Add method to Hash Map */
            result = cfrf_get_method_by_name(&class_file, methods[i].method_name, methods[i].method_signature, &minfo);
            if (result == CFREADER_OK) {
                bytecode = SJAM_NEW(cfrt_bytecode);
                if (bytecode == NULL) {
                    sjaf_warning("Failed to allocate space for bytecode structure");
                    continue;
                }
                
                result = cfrf_get_bytecode(minfo, bytecode);
                if (result == CFREADER_OK) {
                    sjdf_hash_map_put(&sjav_method_id_to_bytecode, methods[i].method_id, bytecode);
                } else {
                    free(bytecode);
                }
            } else {
                sjaf_debug("Failed to locate method (result = %d)", result);
            }
        }
    }
    
    return STARJ_OK;
}

void sjaf_handler_class_load(sjat_thread_id env_id, const char *name, const char *src_name,
        jint num_interfaces, jint num_methods, JVMPI_Method *methods,
        jint num_static_fields, JVMPI_Field *statics,
        jint num_instance_fields, JVMPI_Field *instances,
        sjat_class_id class_id, bool requested) {
    sjat_event event_id = STARJ_EVENT_CLASS_LOAD;
    sjat_field_mask m = sjav_event_masks[event_id];
    jint i;
    
#ifdef STARJ_DBG_TRACE_REQUESTS
    fprintf(sjav_dbg_req_file, "Marking class %d in progress\n", class_id);
#endif
    sjdf_hash_set_add(&sjav_pending_class_ids, class_id);
    process_class(name, num_methods, methods, class_id);

    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_CLASS_NAME) {
        SJAM_WRITE_UTF8(name, sjav_file);
    }
    if (m & STARJ_FIELD_SOURCE_NAME) {
        SJAM_WRITE_UTF8(src_name, sjav_file);
    }
    if (m & STARJ_FIELD_NUM_INTERFACES) {
        SJAM_WRITE_JINT(num_interfaces, sjav_file);
    }
    if ((m & STARJ_FIELD_NUM_METHODS) || (m & STARJ_FIELD_METHODS)) {
        SJAM_WRITE_JINT(num_methods, sjav_file);
    }
    if (m & STARJ_FIELD_METHODS) {
        for (i = 0; i < num_methods; i++) {
            SJAM_WRITE_UTF8(methods[i].method_name, sjav_file);
            SJAM_WRITE_UTF8(methods[i].method_signature, sjav_file);
            SJAM_WRITE_JINT(methods[i].start_lineno, sjav_file);
            SJAM_WRITE_JINT(methods[i].end_lineno, sjav_file);
            SJAM_WRITE_METHOD_ID(methods[i].method_id, sjav_file);
        }
    }
    if ((m & STARJ_FIELD_NUM_STATIC_FIELDS) || (m & STARJ_FIELD_STATICS)) {
        SJAM_WRITE_JINT(num_static_fields, sjav_file);
    }
    if (m & STARJ_FIELD_STATICS) {
        for (i = 0; i < num_static_fields; i++) {
            SJAM_WRITE_UTF8(statics[i].field_name, sjav_file);
            SJAM_WRITE_UTF8(statics[i].field_signature, sjav_file);
        }
    }
    if ((m & STARJ_FIELD_NUM_INSTANCE_FIELDS) || (m & STARJ_FIELD_INSTANCES)) {
        SJAM_WRITE_JINT(num_instance_fields, sjav_file);
    }
    if (m & STARJ_FIELD_INSTANCES) {
        for (i = 0; i < num_instance_fields; i++) {
            SJAM_WRITE_UTF8(instances[i].field_name, sjav_file);
            SJAM_WRITE_UTF8(instances[i].field_signature, sjav_file);
        }
    }
    if (m & STARJ_FIELD_CLASS_LOAD_CLASS_ID) {
        SJAM_WRITE_CLASS_ID(class_id, sjav_file);
    }
    SJAM_END_IO();

#ifdef STARJ_DBG_TRACE_REQUESTS
    fprintf(sjav_dbg_req_file, "Class %d registered\n", class_id);
#endif
    sjdf_hash_set_remove(&sjav_pending_class_ids, class_id);
}

void sjaf_handler_class_unload(sjat_thread_id env_id, sjat_class_id class_id, bool requested) {
    sjat_event event_id = STARJ_EVENT_CLASS_UNLOAD;
    sjat_field_mask m = sjav_event_masks[event_id];
    
    /* FIXME: Remove class and methods from data structures */
    
    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_CLASS_UNLOAD_CLASS_ID) {
        SJAM_WRITE_CLASS_ID(class_id, sjav_file);
    }
    SJAM_END_IO();
}

void sjaf_handler_class_load_hook(const byte *class_data, jint class_data_len,
        byte **new_class_data, jint *new_class_data_len,
        void * (*malloc_f)(unsigned int), bool requested) {
    sjat_event event_id = STARJ_EVENT_CLASS_LOAD_HOOK;
    sjat_field_mask m = sjav_event_masks[event_id];
    
    /* FIXME: Remove class and methods from data structures */
    
    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if ((m & STARJ_FIELD_CLASS_DATA_LEN)
            || (m & STARJ_FIELD_CLASS_DATA)) {
        SJAM_WRITE_JINT(class_data_len, sjav_file);
    }
    if (m & STARJ_FIELD_CLASS_DATA) {
        SJAM_WRITE_BYTES(class_data, class_data_len, sjav_file);
    }
    SJAM_END_IO();
}
