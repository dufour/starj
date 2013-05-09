#include "starj_thread_events.h"
#include "starj_io.h"
#include "starj_spec.h"
#include "starj_requests.h"

void sjaf_handler_thread_end(sjat_thread_id env_id, bool requested) {
    sjat_event event_id = STARJ_EVENT_THREAD_END;
    sjat_field_mask m = sjav_event_masks[event_id];
    
    /* sjaf_ensure_thread_id(env_id, event_id); */
    SJAM_START_GLOBAL_ACCESS();
    sjdf_hash_set_remove(&sjav_known_thread_ids, env_id);
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
    SJAM_END_IO();
}

void sjaf_handler_thread_start(sjat_thread_id env_id, const char *thread_name,
        const char *group_name, const char *parent_name,
        sjat_object_id thread_id, sjat_thread_id thread_env_id, bool requested) {
    sjat_event event_id = STARJ_EVENT_THREAD_START;
    sjat_field_mask m = sjav_event_masks[event_id];
 
    SJAM_START_GLOBAL_ACCESS();
    sjdf_hash_set_add(&sjav_pending_thread_ids, thread_env_id);
    SJAM_END_GLOBAL_ACCESS();

    /* sjaf_ensure_thread_id(env_id, event_id); */
    sjaf_ensure_object_id(thread_id, event_id);

    if (sjdf_hash_set_add(&sjav_known_thread_ids, thread_id) != STARJ_DATA_OK) {
        sjaf_error("Failed to add thread ID to known set");
    }

    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_THREAD_NAME) {
        SJAM_WRITE_UTF8(thread_name, sjav_file);
    }
    if (m & STARJ_FIELD_GROUP_NAME) {
        SJAM_WRITE_UTF8(group_name, sjav_file);
    }
    if (m & STARJ_FIELD_PARENT_NAME) {
        SJAM_WRITE_UTF8(parent_name, sjav_file);
    }
    if (m & STARJ_FIELD_THREAD_ID) {
        SJAM_WRITE_OBJECT_ID(thread_id, sjav_file);
    }
    if (m & STARJ_FIELD_THREAD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(thread_env_id, sjav_file);
    }
    SJAM_END_IO();

    sjdf_hash_set_remove(&sjav_pending_thread_ids, thread_env_id);
}
