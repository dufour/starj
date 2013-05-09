#ifndef _STARJ_ID_RESOLVER_H
#define _STARJ_ID_RESOLVER_H

#include "starj_global.h"

void sjaf_id_resolver_init();

jint sjaf_resolve_raw_monitor_id(sjat_raw_monitor_id id);

jint sjaf_resolve_method_id(sjat_method_id id);

void sjaf_handle_thread_start(sjat_thread_id id, bool requested);
void sjaf_handle_thread_end(sjat_thread_id id, bool requested);
jlong sjaf_resolve_thread_id(sjat_thread_id id);

void sjaf_handle_object_alloc(sjat_object_id id, bool requested);
void sjaf_handle_object_move(sjat_object_id from_id, sjat_object_id to_id, bool requested);
void sjaf_handle_object_free(sjat_object_id id, bool requested);
jlong sjaf_resolve_object_id(sjat_object_id id);

void sjaf_handle_arena_new(sjat_arena_id id, bool requested);
void sjaf_handle_arena_delete(sjat_arena_id id, bool requested);
jlong sjaf_resolve_arena_id(sjat_arena_id id);

#endif
