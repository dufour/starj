#include "starj_monitor_events.h"
#include "starj_io.h"
#include "starj_spec.h"
#include "starj_requests.h"

void sjaf_handler_monitor_contended_enter(sjat_thread_id env_id,
        sjat_object_id object, bool requested) {
    sjat_event event_id = STARJ_EVENT_MONITOR_CONTENDED_ENTER;
    sjat_field_mask m = sjav_event_masks[event_id];
 
    /* sjaf_ensure_thread_id(env_id, event_id); */
    sjaf_ensure_object_id(object, event_id);
    
    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_OBJECT) {
        SJAM_WRITE_OBJECT_ID(object, sjav_file);
    }
    SJAM_END_IO();
}

void sjaf_handler_monitor_contended_entered(sjat_thread_id env_id,
        sjat_object_id object, bool requested) {
    sjat_event event_id = STARJ_EVENT_MONITOR_CONTENDED_ENTERED;
    sjat_field_mask m = sjav_event_masks[event_id];
 
    /* sjaf_ensure_thread_id(env_id, event_id); */
    sjaf_ensure_object_id(object, event_id);
    
    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_OBJECT) {
        SJAM_WRITE_OBJECT_ID(object, sjav_file);
    }
    SJAM_END_IO();
}

void sjaf_handler_monitor_contended_exit(sjat_thread_id env_id,
        sjat_object_id object, bool requested) {
    sjat_event event_id = STARJ_EVENT_MONITOR_CONTENDED_EXIT;
    sjat_field_mask m = sjav_event_masks[event_id];
 
    /* sjaf_ensure_thread_id(env_id, event_id); */
    sjaf_ensure_object_id(object, event_id);
    
    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_OBJECT) {
        SJAM_WRITE_OBJECT_ID(object, sjav_file);
    }
    SJAM_END_IO();
}

void sjaf_handler_monitor_wait(sjat_thread_id env_id,
        sjat_object_id object, jlong timeout, bool requested) {
    sjat_event event_id = STARJ_EVENT_MONITOR_WAIT;
    sjat_field_mask m = sjav_event_masks[event_id];
 
    /* sjaf_ensure_thread_id(env_id, event_id); */
    sjaf_ensure_object_id(object, event_id);
    
    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_OBJECT) {
        SJAM_WRITE_OBJECT_ID(object, sjav_file);
    }
    if (m & STARJ_FIELD_TIMEOUT) {
        SJAM_WRITE_JLONG(timeout, sjav_file);
    }
    SJAM_END_IO();
}

void sjaf_handler_monitor_waited(sjat_thread_id env_id,
        sjat_object_id object, jlong timeout, bool requested) {
    sjat_event event_id = STARJ_EVENT_MONITOR_WAITED;
    sjat_field_mask m = sjav_event_masks[event_id];
 
    /* sjaf_ensure_thread_id(env_id, event_id); */
    sjaf_ensure_object_id(object, event_id);
    
    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_OBJECT) {
        SJAM_WRITE_OBJECT_ID(object, sjav_file);
    }
    if (m & STARJ_FIELD_TIMEOUT) {
        SJAM_WRITE_JLONG(timeout, sjav_file);
    }
    SJAM_END_IO();
}

void sjaf_handler_raw_monitor_contended_enter(sjat_thread_id env_id,
        const char *name, sjat_raw_monitor_id id, bool requested) {
    sjat_event event_id = STARJ_EVENT_RAW_MONITOR_CONTENDED_ENTER;
    sjat_field_mask m = sjav_event_masks[event_id];
 
    /* sjaf_ensure_thread_id(env_id, event_id); */
    
    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_NAME) {
        SJAM_WRITE_UTF8(name, sjav_file);
    }
    if (m & STARJ_FIELD_ID) {
        SJAM_WRITE_RAW_MONITOR_ID(id, sjav_file);
    }
    SJAM_END_IO();
}

void sjaf_handler_raw_monitor_contended_entered(sjat_thread_id env_id,
        const char *name, sjat_raw_monitor_id id, bool requested) {
    sjat_event event_id = STARJ_EVENT_RAW_MONITOR_CONTENDED_ENTERED;
    sjat_field_mask m = sjav_event_masks[event_id];
 
    /* sjaf_ensure_thread_id(env_id, event_id); */
    
    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_NAME) {
        SJAM_WRITE_UTF8(name, sjav_file);
    }
    if (m & STARJ_FIELD_ID) {
        SJAM_WRITE_RAW_MONITOR_ID(id, sjav_file);
    }
    SJAM_END_IO();
}

void sjaf_handler_raw_monitor_contended_exit(sjat_thread_id env_id,
        const char *name, sjat_raw_monitor_id id, bool requested) {
    sjat_event event_id = STARJ_EVENT_RAW_MONITOR_CONTENDED_EXIT;
    sjat_field_mask m = sjav_event_masks[event_id];
 
    /* sjaf_ensure_thread_id(env_id, event_id); */
    
    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_NAME) {
        SJAM_WRITE_UTF8(name, sjav_file);
    }
    if (m & STARJ_FIELD_ID) {
        SJAM_WRITE_RAW_MONITOR_ID(id, sjav_file);
    }
    SJAM_END_IO();
}
