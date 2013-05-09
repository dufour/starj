#include "starj_jvm_events.h"
#include "starj_io.h"
#include "starj_spec.h"
#include "starj_requests.h"

void sjaf_handler_jvm_init_done(sjat_thread_id env_id, bool requested) {
    sjat_event event_id = STARJ_EVENT_JVM_INIT_DONE;
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
    SJAM_END_IO();
}

void sjaf_handler_jvm_shut_down(sjat_thread_id env_id, bool requested) {
    sjat_event event_id = STARJ_EVENT_JVM_SHUT_DOWN;
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
    SJAM_END_IO();
}
