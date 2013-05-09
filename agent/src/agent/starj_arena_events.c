#include "starj_arena_events.h"
#include "starj_io.h"
#include "starj_spec.h"
#include "starj_requests.h"

void remove_from_known_objects(sjdt_key object_id) {
    sjdf_hash_set_remove(&sjav_known_object_ids, object_id);
}

void sjaf_handler_arena_delete(sjat_thread_id env_id, sjat_arena_id arena_id, 
        bool requested) {
    sjdt_hash_set *set;
    sjat_event event_id = STARJ_EVENT_ARENA_DELETE;
    sjat_field_mask m = sjav_event_masks[event_id];
 
    /* sjaf_ensure_thread_id(env_id, event_id); */
 
    SJAM_START_GLOBAL_ACCESS();
    if (sjdf_hash_map_get(&sjav_arena_id_to_objects, (sjdt_key) arena_id, (sjdt_value *) &set) == STARJ_DATA_OK) {
        if (set != NULL) {
            sjdf_hash_set_apply(set, remove_from_known_objects);
            sjdf_hash_map_remove(&sjav_arena_id_to_objects, (sjdt_key) arena_id);
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
    SJAM_END_IO();
}

void sjaf_handler_arena_new(sjat_thread_id env_id, sjat_arena_id arena_id,
        const char *arena_name, bool requested) {
    sjdt_hash_set *set;
    sjat_event event_id = STARJ_EVENT_ARENA_NEW;
    sjat_field_mask m = sjav_event_masks[event_id];
    
    /* sjaf_ensure_thread_id(env_id, event_id); */

    SJAM_START_GLOBAL_ACCESS();
    sjdf_hash_map_remove(&sjav_arena_id_to_objects, (sjdt_key) arena_id);
    set = SJAM_NEW(sjdt_hash_set);
    if (set != NULL) {
        if (sjdf_hash_set_init(set, 64, 0.75, NULL, NULL, NULL, NULL) == STARJ_DATA_OK) {
            if (sjdf_hash_map_put(&sjav_arena_id_to_objects, (sjdt_key) arena_id, set) != STARJ_DATA_OK) {
                sjaf_error("Failed to bind arena_id to newly created set");
                free(set);
            }
        } else {
            sjaf_error("Failed to initialize new arena set");
        }
    } else {
        sjaf_error("Failed to allocate a new arena set");
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
    if (m & STARJ_FIELD_ARENA_NAME) {
        SJAM_WRITE_UTF8(arena_name, sjav_file);
    }
    SJAM_END_IO();
}
