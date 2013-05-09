#ifndef _STARJ_OBJECT_EVENTS_H
#define _STARJ_OBJECT_EVENTS_H

#include "starj_global.h"

void sjaf_handler_object_alloc(sjat_thread_id env_id, sjat_arena_id arena_id,
        sjat_class_id class_id, jint is_array, jint size, sjat_object_id obj_id,
        bool requested);
void sjaf_handler_object_dump(sjat_thread_id env_id, jint data_len,
        const byte *data, bool requested);
void sjaf_handler_object_free(sjat_thread_id env_id, jobjectID obj_id,
        bool requested);
void sjaf_handler_object_move(sjat_thread_id env_id, sjat_arena_id arena_id,
        sjat_object_id obj_id, sjat_arena_id new_arena_id,
        sjat_object_id new_obj_id, bool requested);

#endif
