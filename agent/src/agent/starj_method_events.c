#include "starj_method_events.h"
#include "starj_io.h"
#include "starj_spec.h"
#include "starj_requests.h"

void sjaf_handler_method_entry(sjat_thread_id env_id, sjat_method_id method_id,
        bool requested) {
    sjat_event event_id = STARJ_EVENT_METHOD_ENTRY;
    sjat_field_mask m = sjav_event_masks[event_id];

    /* sjaf_ensure_thread_id(env_id, event_id); */
    sjaf_ensure_method_id(method_id, event_id);

    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_METHOD_ID) {
        SJAM_WRITE_METHOD_ID(method_id, sjav_file);
    }
    SJAM_END_IO();
}

void sjaf_handler_method_entry2(sjat_thread_id env_id, sjat_method_id method_id,
        sjat_object_id obj_id, bool requested) {
    sjat_event event_id = STARJ_EVENT_METHOD_ENTRY2;
    sjat_field_mask m = sjav_event_masks[event_id];

    /* sjaf_ensure_thread_id(env_id, event_id); */
    sjaf_ensure_method_id(method_id, event_id);
    sjaf_ensure_object_id(obj_id, event_id);

    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_METHOD_ID) {
        SJAM_WRITE_METHOD_ID(method_id, sjav_file);
    }
    if (m & STARJ_FIELD_OBJ_ID) {
        SJAM_WRITE_OBJECT_ID(obj_id, sjav_file);
    }
    SJAM_END_IO();
}

void sjaf_handler_method_exit(sjat_thread_id env_id, sjat_method_id method_id,
        bool requested) {
    sjat_event event_id = STARJ_EVENT_METHOD_EXIT;
    sjat_field_mask m = sjav_event_masks[event_id];

    /* sjaf_ensure_thread_id(env_id, event_id); */
    sjaf_ensure_method_id(method_id, event_id);

    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_METHOD_ID) {
        SJAM_WRITE_METHOD_ID(method_id, sjav_file);
    }
    SJAM_END_IO();
}
