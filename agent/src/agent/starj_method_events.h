#ifndef _STARJ_METHOD_EVENTS_H
#define _STARJ_METHOD_EVENTS_H

#include "starj_global.h"

void sjaf_handler_method_entry(sjat_thread_id env_id, sjat_method_id method_id,
        bool requested);
void sjaf_handler_method_entry2(sjat_thread_id env_id, sjat_method_id method_id,
        sjat_object_id obj_id, bool requested);
void sjaf_handler_method_exit(sjat_thread_id env_id, sjat_method_id method_id,
        bool requested);
#endif
