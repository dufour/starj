#ifndef _STARJ_HASH_MAP_H
#define _STARJ_HASH_MAP_H

#include "starj_data_global.h"

typedef struct hash_map_node {
    sjdt_key key;
    sjdt_value value;
    struct hash_map_node *next;
} sjdt_hash_map_node;

typedef struct hash_map {
    sjdt_hash_map_node **data;
    size_t capacity;
    size_t size;
    double load_factor;

    sjdt_hash_fn hash_fn;
    
    sjdt_key_comp_fn key_comp_fn;
    sjdt_key_clone_fn key_clone_fn;
    sjdt_key_release_fn key_release_fn;
    
    sjdt_value_comp_fn value_comp_fn;
    sjdt_value_clone_fn value_clone_fn;
    sjdt_value_release_fn value_release_fn;
} sjdt_hash_map;

int sjdf_hash_map_init(sjdt_hash_map *map, size_t size, double load_factor,
        sjdt_hash_fn hash_fn, sjdt_key_comp_fn key_comp_fn,
        sjdt_key_clone_fn key_clone_fn, sjdt_key_release_fn key_release_fn,
        sjdt_value_comp_fn value_comp_fn, sjdt_value_clone_fn value_clone_fn,
        sjdt_value_release_fn value_release_fn);
void sjdf_hash_map_release(sjdt_hash_map *map);
int sjdf_hash_map_put(sjdt_hash_map *map, const sjdt_key key, sjdt_value value);
int sjdf_hash_map_get(sjdt_hash_map *map, const sjdt_key key, sjdt_value *value);
int sjdf_hash_map_move(sjdt_hash_map *map, const sjdt_key old_key,
        sjdt_key new_key);
int sjdf_hash_map_remove(sjdt_hash_map *map, const sjdt_key key);
int sjdf_hash_map_contains_key(sjdt_hash_map *map, const sjdt_key key);
int sjdf_hash_map_contains_value(sjdt_hash_map *map, sjdt_value value);
int sjdf_hash_map_resize(sjdt_hash_map *map, size_t new_capacity);

#endif
