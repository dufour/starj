#include "starj_gc_events.h"
#include "starj_io.h"
#include "starj_spec.h"
#include "starj_requests.h"

void sjaf_handler_gc_finish(sjat_thread_id env_id, jlong used_objects,
        jlong used_object_space, jlong total_object_space, bool requested) {
    sjat_event event_id = STARJ_EVENT_GC_FINISH;
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
    if (m & STARJ_FIELD_USED_OBJECTS) {
        SJAM_WRITE_JLONG(used_objects, sjav_file);
    }
    if (m & STARJ_FIELD_USED_OBJECT_SPACE) {
        SJAM_WRITE_JLONG(used_object_space, sjav_file);
    }
    if (m & STARJ_FIELD_TOTAL_OBJECT_SPACE) {
        SJAM_WRITE_JLONG(total_object_space, sjav_file);
    }
    SJAM_END_IO();
}

void sjaf_handler_gc_start(sjat_thread_id env_id, bool requested) {
    sjat_event event_id = STARJ_EVENT_GC_START;
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
