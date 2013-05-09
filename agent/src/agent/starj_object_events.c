#include "starj_object_events.h"
#include "starj_io.h"
#include "starj_spec.h"
#include "starj_requests.h"

void sjaf_handler_object_alloc(sjat_thread_id env_id, sjat_arena_id arena_id,
        sjat_class_id class_id, jint is_array, jint size, sjat_object_id obj_id,
        bool requested) {
    sjdt_hash_set *set;
    sjat_event event_id = STARJ_EVENT_OBJECT_ALLOC;
    sjat_field_mask m = sjav_event_masks[event_id];

#ifdef STARJ_DBG_TRACE_REQUESTS
    fprintf(sjav_dbg_req_file, "Marking object %d in progress\n", obj_id);
#endif
    sjdf_hash_set_add(&sjav_pending_object_ids, obj_id);

    /* sjaf_ensure_thread_id(env_id, event_id); */
    sjaf_ensure_class_id(class_id, event_id);

    SJAM_START_GLOBAL_ACCESS();
    if (sjdf_hash_set_add(&sjav_known_object_ids, obj_id) != STARJ_DATA_OK) {
        sjaf_error("Failed to add object ID to known set");
    }
    
    if (sjdf_hash_map_get(&sjav_arena_id_to_objects, (sjdt_key) arena_id, (sjdt_value *) &set) == STARJ_DATA_OK) {
        if (sjdf_hash_set_add(set, obj_id) != STARJ_DATA_OK) {
            sjaf_error("Failed to add object ID to arena");
        }
    }
    SJAM_END_GLOBAL_ACCESS();

    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_ARENA_ID) {
        SJAM_WRITE_ARENA_ID(arena_id, sjav_file);
    }
    if (m & STARJ_FIELD_OBJECT_ALLOC_CLASS_ID) {
        SJAM_WRITE_CLASS_ID(class_id, sjav_file);
    }
    if (m & STARJ_FIELD_IS_ARRAY) {
        SJAM_WRITE_JINT(is_array, sjav_file);
    }
    if (m & STARJ_FIELD_SIZE) {
        SJAM_WRITE_JINT(size, sjav_file);
    }
    if (m & STARJ_FIELD_OBJ_ID) {
        SJAM_WRITE_OBJECT_ID(obj_id, sjav_file);
    }
    SJAM_END_IO(); 

#ifdef STARJ_DBG_TRACE_REQUESTS
    fprintf(sjav_dbg_req_file, "Object %d registered\n", obj_id);
#endif
    sjdf_hash_set_remove(&sjav_pending_object_ids, obj_id);
}

void sjaf_handler_object_dump(sjat_thread_id env_id, jint data_len,
        const byte *data, bool requested) {
    /* FIXME */
}

void sjaf_handler_object_free(sjat_thread_id env_id, jobjectID obj_id,
        bool requested) {
    sjat_event event_id = STARJ_EVENT_OBJECT_FREE;
    sjat_field_mask m = sjav_event_masks[event_id];
    
    /* sjaf_ensure_thread_id(env_id, event_id); */
    sjaf_ensure_object_id(obj_id, event_id);

    SJAM_START_GLOBAL_ACCESS();
    sjdf_hash_set_remove(&sjav_known_object_ids, obj_id);
    SJAM_END_GLOBAL_ACCESS();

    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_OBJ_ID) {
        SJAM_WRITE_OBJECT_ID(obj_id, sjav_file);
    }
    SJAM_END_IO();
}

void sjaf_handler_object_move(sjat_thread_id env_id, sjat_arena_id arena_id,
        sjat_object_id obj_id, sjat_arena_id new_arena_id,
        sjat_object_id new_obj_id, bool requested) {
    sjat_event event_id = STARJ_EVENT_OBJECT_MOVE;
    sjat_field_mask m = sjav_event_masks[event_id];
    sjdt_hash_set *set;
    
    /* IMPORTANT NOTE: do *not* request an OBJECT_ALLOC event here,
     * even if the ID is still undefined. Doing so would crash the VM.
     */

    SJAM_START_GLOBAL_ACCESS();
    if (sjdf_hash_set_contains(&sjav_known_object_ids, obj_id) == STARJ_DATA_OK) {
        sjdf_hash_set_remove(&sjav_known_object_ids, obj_id);
        sjdf_hash_set_add(&sjav_known_object_ids, new_obj_id);
        sjdf_hash_map_move(&sjav_class_id_to_methods, obj_id, new_obj_id);
        if (sjdf_hash_map_get(&sjav_arena_id_to_objects, (sjdt_key) arena_id, (sjdt_value *) &set) == STARJ_DATA_OK) {
            sjdf_hash_set_remove(set, obj_id);
        }
        if (sjdf_hash_map_get(&sjav_arena_id_to_objects, (sjdt_key) new_arena_id, (sjdt_value *) &set) == STARJ_DATA_OK) {
            sjdf_hash_set_add(set, new_obj_id);
        }
    }
    SJAM_END_GLOBAL_ACCESS();
    
    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }
    
    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_ARENA_ID) {
        SJAM_WRITE_ARENA_ID(arena_id, sjav_file);
    }
    if (m & STARJ_FIELD_OBJ_ID) {
        SJAM_WRITE_OBJECT_ID(obj_id, sjav_file);
    }
    if (m & STARJ_FIELD_NEW_ARENA_ID) {
        SJAM_WRITE_ARENA_ID(new_arena_id, sjav_file);
    }
    if (m & STARJ_FIELD_NEW_OBJ_ID) {
        SJAM_WRITE_OBJECT_ID(new_obj_id, sjav_file);
    }
    SJAM_END_IO();

    sjdf_hash_set_remove(&sjav_pending_object_ids, new_obj_id);
}
