#include "starj_requests.h"
#include "starj_io.h"
#include "../data/starj_hash_set.h"

SJAM_INLINE void sjaf_ensure_thread_id(sjat_thread_id env_id, sjat_event handler) {
    if (env_id != NULL) {
        SJAM_START_GLOBAL_ACCESS();
        if (sjdf_hash_set_contains(&sjav_known_thread_ids, env_id) != STARJ_DATA_OK) {
            /* We haven't seen any defining event for this thread yet */
            sjat_object_id thread_obj_id;

            if (sjdf_hash_set_contains(&sjav_pending_thread_ids, env_id) == STARJ_DATA_OK) {
                /* We are already processing this ID */
                SJAM_END_GLOBAL_ACCESS();
                return;
            }

            sjdf_hash_set_add(&sjav_pending_thread_ids, env_id);
            
            SJAM_CALL(DisableGC)();
            thread_obj_id = SJAM_CALL(GetThreadObject)(env_id);
            SJAM_CALL(EnableGC)();

            if (thread_obj_id != NULL) {
                sjaf_ensure_object_id(thread_obj_id, handler);

                if (SJAM_CALL(RequestEvent)(JVMPI_EVENT_THREAD_START, thread_obj_id) != JVMPI_SUCCESS) {
                    sjaf_error("Request for %s event failed in %s handler",
                            sjav_event_names[STARJ_EVENT_THREAD_START],
                            sjav_event_names[handler]);
                }
            }
        }
        SJAM_END_GLOBAL_ACCESS();
    }
}

SJAM_INLINE void sjaf_ensure_object_id(sjat_object_id obj_id, sjat_event handler) {
    if (obj_id != NULL) {
        SJAM_START_GLOBAL_ACCESS();
#ifdef STARJ_DBG_TRACE_REQUESTS
        fprintf(sjav_dbg_req_file, "  Ensuring validity of object %d\n",
                obj_id);
#endif
        if (sjdf_hash_set_contains(&sjav_pending_object_ids, obj_id) == STARJ_DATA_OK) {
            /* We are already processing this ID */
#ifdef STARJ_DBG_TRACE_REQUESTS
            fprintf(sjav_dbg_req_file, "  Object %d already in progree\n",
                    obj_id);
#endif
            SJAM_END_GLOBAL_ACCESS();
            return;
        }
        if (sjdf_hash_set_contains(&sjav_known_object_ids, obj_id) != STARJ_DATA_OK) {
            /* We haven't seen any defining event for this object yet */
#ifdef STARJ_DBG_TRACE_REQUESTS
            fprintf(sjav_dbg_req_file, "  Marking object %d in progress\n",
                    obj_id);
#endif
            sjdf_hash_set_add(&sjav_pending_object_ids, obj_id);
#ifdef STARJ_DBG_TRACE_REQUESTS
            fprintf(sjav_dbg_req_file, "  Requesting object %d\n",
                    obj_id);
#endif
            if (SJAM_CALL(RequestEvent)(JVMPI_EVENT_OBJECT_ALLOC, obj_id) != JVMPI_SUCCESS) {
                sjaf_error("Request for %s event failed in %s handler",
                        sjav_event_names[STARJ_EVENT_OBJECT_ALLOC],
                        sjav_event_names[handler]);
            }
        }
#ifdef STARJ_DBG_TRACE_REQUESTS
          else {
            fprintf(sjav_dbg_req_file, "  Object %d already registered\n",
                    obj_id);
        }
#endif
        SJAM_END_GLOBAL_ACCESS();
    }
}

SJAM_INLINE void sjaf_ensure_class_id(sjat_class_id class_id, sjat_event handler) {
    if (class_id != NULL) {
        SJAM_START_GLOBAL_ACCESS();
#ifdef STARJ_DBG_TRACE_REQUESTS
        fprintf(sjav_dbg_req_file, "Ensuring validity of class %d\n",
                class_id);
#endif
        if (sjdf_hash_set_contains(&sjav_pending_class_ids, class_id) == STARJ_DATA_OK) {
#ifdef STARJ_DBG_TRACE_REQUESTS
        fprintf(sjav_dbg_req_file, "  Class %d already in progress\n", class_id);
#endif
            SJAM_END_GLOBAL_ACCESS();
            return;
        }

        sjaf_ensure_object_id(class_id, handler);

        if (sjdf_hash_map_contains_key(&sjav_class_id_to_methods, class_id) != STARJ_DATA_OK) {
            /* We haven't seen any defining event for this class yet */
#ifdef STARJ_DBG_TRACE_REQUESTS
            fprintf(sjav_dbg_req_file, "  Marking class %d in progress\n",
                    class_id);
#endif
            sjdf_hash_set_add(&sjav_pending_class_ids, class_id);

#ifdef STARJ_DBG_TRACE_REQUESTS
            fprintf(sjav_dbg_req_file, "  Requesting class %d\n",
                    class_id);
#endif
            if (SJAM_CALL(RequestEvent)(JVMPI_EVENT_CLASS_LOAD, class_id) != JVMPI_SUCCESS) {
                sjaf_error("Request for %s event failed in %s handler",
                        sjav_event_names[STARJ_EVENT_CLASS_LOAD],
                        sjav_event_names[handler]);
            }
        }
#ifdef STARJ_DBG_TRACE_REQUESTS
          else {
            fprintf(sjav_dbg_req_file, "  Class %d already registered\n",
                    class_id);
        }
#endif
        SJAM_END_GLOBAL_ACCESS();
    }
}

SJAM_INLINE void sjaf_ensure_method_id(sjat_method_id method_id, sjat_event handler) {
    if (method_id != NULL) {
        sjat_object_id defining_class_id;

#ifdef STARJ_DBG_TRACE_REQUESTS
        fprintf(sjav_dbg_req_file, "Ensuring validity of method %d\n",
                method_id);
#endif
        
        SJAM_START_GLOBAL_ACCESS();
        if (sjdf_hash_set_contains(&sjav_known_method_ids, method_id) == STARJ_DATA_OK) {
#ifdef STARJ_DBG_TRACE_REQUESTS
            fprintf(sjav_dbg_req_file, "  Method %d already registered\n",
                    method_id);
#endif
            SJAM_END_GLOBAL_ACCESS();
            return;
        }

        SJAM_CALL(DisableGC)();
        defining_class_id = SJAM_CALL(GetMethodClass)(method_id);
        SJAM_CALL(EnableGC)();

#ifdef STARJ_DBG_TRACE_REQUESTS
        fprintf(sjav_dbg_req_file, "  Method %d defined by class %d\n",
                method_id, defining_class_id);
#endif
        sjaf_ensure_class_id(defining_class_id, handler);
        SJAM_END_GLOBAL_ACCESS();
    }
}
