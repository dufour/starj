#ifndef _STARJ_THREAD_EVENTS_H
#define _STARJ_THREAD_EVENTS_H

#include "starj_global.h"

void sjaf_handler_thread_end(sjat_thread_id env_id, bool requested);
void sjaf_handler_thread_start(sjat_thread_id env_id, const char *thread_name,
        const char *group_name, const char *parent_name,
        sjat_object_id thread_id, sjat_thread_id thread_env_id, bool requested);

#endif
