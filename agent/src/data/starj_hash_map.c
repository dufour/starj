#include "starj_hash_map.h"
#include "starj_data_util.h"

#include <stdio.h>

int sjdf_hash_map_init(sjdt_hash_map *map, size_t size, double load_factor,
        sjdt_hash_fn hash_fn, sjdt_key_comp_fn key_comp_fn, sjdt_key_clone_fn key_clone_fn,
        sjdt_key_release_fn key_release_fn, sjdt_value_comp_fn value_comp_fn,
        sjdt_value_clone_fn value_clone_fn, sjdt_value_release_fn value_release_fn) {
    if (map != NULL) {
        if (size < 0) {
            return STARJ_DATA_ERR_NEG_SIZE;
        }

        map->data = SJDM_NEW_ARRAY(sjdt_hash_map_node *, size);
        if (map->data != NULL) {
            map->capacity = size;
            map->load_factor = load_factor;
            map->size = 0;

            map->hash_fn = ((hash_fn != NULL) ? hash_fn : sjdf_identity_hash);

            map->key_comp_fn = ((key_comp_fn != NULL) ? key_comp_fn : sjdf_generic_key_comp);
            map->key_clone_fn = ((key_clone_fn != NULL) ? key_clone_fn : sjdf_generic_key_clone);
            map->key_release_fn = ((key_release_fn != NULL) ? key_release_fn : sjdf_generic_key_release);

            map->value_comp_fn = ((value_comp_fn != NULL) ? value_comp_fn : sjdf_generic_value_comp);
            map->value_clone_fn = ((value_clone_fn != NULL) ? value_clone_fn : sjdf_generic_value_clone);
            map->value_release_fn = ((value_release_fn != NULL) ? value_release_fn : sjdf_generic_value_release);

            return STARJ_DATA_OK;
        } else {
            return STARJ_DATA_ERR_MALLOC;
        }
    }

    return STARJ_DATA_ERR_NULL_PTR;
}

void sjdf_hash_map_release(sjdt_hash_map *map) {
    if (map != NULL) {
        size_t i;

        for (i = 0; i < map->capacity; i++) {
            sjdt_hash_map_node *node;
            sjdt_hash_map_node *tmp;

            for (node = map->data[i]; node != NULL; ) {
                tmp = node->next;

                map->key_release_fn(node->key);
                map->value_release_fn(node->value);
                free(node);

                node = tmp;
            }
        }
    }
}

int sjdf_hash_map_put(sjdt_hash_map *map, const sjdt_key key, sjdt_value value) {
    size_t index;
    sjdt_hash_map_node *node;

    if (map == NULL) {
        return STARJ_DATA_ERR_NULL_PTR;
    }

    index = map->hash_fn(key) % map->capacity;
    for (node = map->data[index]; node != NULL; node = node->next) {
        if (!map->key_comp_fn(node->key, key)) {
            /* Found a match */
            map->value_release_fn(node->value);
            node->value = map->value_clone_fn(value);

            return STARJ_DATA_OK;
        }
    }

    node = SJDM_NEW(sjdt_hash_map_node);
    if (node != NULL) {
        node->next = map->data[index];
        node->key = map->key_clone_fn(key);
        node->value = map->value_clone_fn(value);
        map->data[index] = node;
        map->size += 1;

        if ((map->load_factor > 0.0)
                && (map->size > (map->capacity * map->load_factor))) {
            return sjdf_hash_map_resize(map, 2 * map->capacity + 1);
        }

        return STARJ_DATA_OK;
    }

    return STARJ_DATA_ERR_MALLOC;
}

int sjdf_hash_map_get(sjdt_hash_map *map, const sjdt_key key, sjdt_value *value) {
    size_t index;
    sjdt_hash_map_node *node;

    if ((map == NULL) || (value == NULL)) {
        return STARJ_DATA_ERR_NULL_PTR;
    }

    index = map->hash_fn(key) % map->capacity;
    for (node = map->data[index]; node != NULL; node = node->next) {
        if (!map->key_comp_fn(node->key, key)) {
            /* Found a match */
            *value = node->value;
            return STARJ_DATA_OK;
        }
    }

    return STARJ_DATA_ERR_NOT_FOUND;
}

int sjdf_hash_map_move(sjdt_hash_map *map, const sjdt_key old_key, sjdt_key new_key) {
    size_t index;
    sjdt_hash_map_node *node;
    sjdt_hash_map_node *tmp;

    if (map == NULL) {
        return STARJ_DATA_ERR_NULL_PTR;
    }

    index = map->hash_fn(old_key) % map->capacity;
    tmp = NULL;
    for (node = map->data[index]; node != NULL; node = node->next) {
        if (!map->key_comp_fn(node->key, old_key)) {
            size_t new_index;
            /* Found a match */
            if (tmp != NULL) {
                tmp->next = node->next;
            } else {
                map->data[index] = node->next;
            }

            new_index = map->hash_fn(new_key) % map->capacity;
            node->next = map->data[new_index];
            map->data[new_index] = node;
            return STARJ_DATA_OK;
        }

        tmp = node;
    } 

    return STARJ_DATA_ERR_NOT_FOUND;
}

int sjdf_hash_map_remove(sjdt_hash_map *map, const sjdt_key key) {
    size_t index;
    sjdt_hash_map_node *node;
    sjdt_hash_map_node *tmp;

    if (map == NULL) {
        return STARJ_DATA_ERR_NULL_PTR;
    }

    index = map->hash_fn(key) % map->capacity;
    tmp = NULL;
    for (node = map->data[index]; node != NULL; node = node->next) {
        if (!map->key_comp_fn(node->key, key)) {
            /* Found a match */
            if (tmp != NULL) {
                tmp->next = node->next;
            } else {
                map->data[index] = node->next;
            }

            map->key_release_fn(node->key);
            map->value_release_fn(node->value);
            free(node);
            map->size -= 1;
            return STARJ_DATA_OK;
        }

        tmp = node;
    }

    return STARJ_DATA_ERR_NOT_FOUND;
}

int sjdf_hash_map_contains_key(sjdt_hash_map *map, const sjdt_key key) {
    size_t index;
    sjdt_hash_map_node *node;

    if (map == NULL) {
        return STARJ_DATA_ERR_NULL_PTR;
    }

    index = map->hash_fn(key) % map->capacity;
    for (node = map->data[index]; node != NULL; node = node->next) {
        if (!map->key_comp_fn(node->key, key)) {
            /* Found a match */
            return STARJ_DATA_OK;
        }
    }

    return STARJ_DATA_ERR_NOT_FOUND;
}

int sjdf_hash_map_contains_value(sjdt_hash_map *map, sjdt_value value) {
    size_t index;
    sjdt_hash_map_node *node;

    if (map == NULL) {
        return STARJ_DATA_ERR_NULL_PTR;
    }

    for (index = 0; index < map->capacity; index++) {
        for (node = map->data[index]; node != NULL; node = node->next) {
            if (!map->value_comp_fn(node->value, value)) {
                return STARJ_DATA_OK;
            }
        }
    }

    return STARJ_DATA_ERR_NOT_FOUND;
}

int sjdf_hash_map_resize(sjdt_hash_map *map, size_t new_capacity) {
    sjdt_hash_map_node **new_data;

    if (map == NULL) {
        return STARJ_DATA_ERR_NULL_PTR;
    }

    if (new_capacity < 0) {
        return STARJ_DATA_ERR_NEG_SIZE;
    }
    
    new_data = SJDM_NEW_ARRAY(sjdt_hash_map_node *, new_capacity);
    if (new_data != NULL) {
        size_t i;
        size_t index;
        sjdt_hash_map_node *node;
        sjdt_hash_map_node *tmp;

        for (i = 0; i < map->capacity; i++) {
            for (node = map->data[i]; node != NULL; ) {
                tmp = node->next;

                index = map->hash_fn(node->key) % new_capacity;
                node->next = new_data[index];
                new_data[index] = node;
                
                node = tmp;
            }
        }

        free(map->data);
        map->data = new_data;
        map->capacity = new_capacity;
        
        return STARJ_DATA_OK;
    }

    return STARJ_DATA_ERR_MALLOC;
}
