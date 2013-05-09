#ifndef _STARJ_HASH_SET_H
#define _STARJ_HASH_SET_H

#include "starj_data_global.h"

typedef struct hash_set_node {
    sjdt_key value;
    struct hash_set_node *next;
} sjdt_hash_set_node;

typedef struct hash_set {
    sjdt_hash_set_node **data;
    size_t capacity;
    size_t size;
    double load_factor;

    sjdt_hash_fn hash_fn;
    sjdt_key_comp_fn comp_fn;
    sjdt_key_clone_fn clone_fn;
    sjdt_key_release_fn release_fn;
} sjdt_hash_set;

int sjdf_hash_set_init(sjdt_hash_set *set, size_t size, double load_factor,
        sjdt_hash_fn hash_fn, sjdt_key_comp_fn comp_fn, sjdt_key_clone_fn clone_fn,
        sjdt_key_release_fn release_fn);
void sjdf_hash_set_release(sjdt_hash_set *set);
int sjdf_hash_set_add(sjdt_hash_set *set, sjdt_key value);
int sjdf_hash_set_remove(sjdt_hash_set *set, const sjdt_key value);
int sjdf_hash_set_contains(sjdt_hash_set *set, const sjdt_key value);
int sjdf_hash_set_resize(sjdt_hash_set *set, size_t new_capacity);
int sjdf_hash_set_apply(sjdt_hash_set *set, sjdt_key_fn f);

#endif
