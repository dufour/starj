#ifndef _STARJ_INSTRUCTION_EVENTS_H
#define _STARJ_INSTRUCTION_EVENTS_H

#include "starj_global.h"

void sjaf_clear_pending_ise();
void sjaf_handler_instruction_start(sjat_thread_id env_id, sjat_method_id method_id,
        jint offset, jboolean is_true, jint key, jint low, jint hi,
        jint chosen_pair_index, jint pairs_total, bool requested);


#endif
