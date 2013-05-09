#ifndef _STARJ_ARENA_EVENTS_H
#define _STARJ_ARENA_EVENTS_H

#include "starj_global.h"

void sjaf_handler_arena_delete(sjat_thread_id env_id, sjat_arena_id arena_id, 
        bool requested);
void sjaf_handler_arena_new(sjat_thread_id env_id, sjat_arena_id arena_id,
        const char *arena_name, bool requested);

#endif
