#ifndef _STARJ_JVM_EVENTS_H
#define _STARJ_JVM_EVENTS_H

#include "starj_global.h"

void sjaf_handler_jvm_init_done(sjat_thread_id env_id, bool requested);
void sjaf_handler_jvm_shut_down(sjat_thread_id env_id, bool requested);

#endif
