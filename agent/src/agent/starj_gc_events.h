#ifndef _STARJ_GC_EVENTS_H
#define _STARJ_GC_EVENTS_H

#include "starj_global.h"

void sjaf_handler_gc_start(sjat_thread_id env_id, bool requested);
void sjaf_handler_gc_finish(sjat_thread_id env_id, jlong used_objects,
        jlong used_object_space, jlong total_object_space, bool requested);

#endif
