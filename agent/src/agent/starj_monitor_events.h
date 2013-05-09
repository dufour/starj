#ifndef _STARJ_MONITOR_EVENTS_H
#define _STARJ_MONITOR_EVENTS_H

#include "starj_global.h"

void sjaf_handler_monitor_contended_enter(sjat_thread_id env_id,
        sjat_object_id object, bool requested);
void sjaf_handler_monitor_contended_entered(sjat_thread_id env_id,
        sjat_object_id object, bool requested);
void sjaf_handler_monitor_contended_exit(sjat_thread_id env_id,
        sjat_object_id object, bool requested);
    
void sjaf_handler_monitor_wait(sjat_thread_id env_id,
        sjat_object_id object, jlong timeout, bool requested);
void sjaf_handler_monitor_waited(sjat_thread_id env_id,
        sjat_object_id object, jlong timeout, bool requested);

void sjaf_handler_raw_monitor_contended_enter(sjat_thread_id env_id,
        const char *name, sjat_raw_monitor_id id, bool requested);
void sjaf_handler_raw_monitor_contended_entered(sjat_thread_id env_id,
        const char *name, sjat_raw_monitor_id id, bool requested);
void sjaf_handler_raw_monitor_contended_exit(sjat_thread_id env_id,
        const char *name, sjat_raw_monitor_id id, bool requested);
#endif
