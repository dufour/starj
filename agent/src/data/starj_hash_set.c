#include "starj_hash_set.h"
#include "starj_data_util.h"

int sjdf_hash_set_init(sjdt_hash_set *set, size_t size, double load_factor,
        sjdt_hash_fn hash_fn, sjdt_key_comp_fn comp_fn,
        sjdt_key_clone_fn clone_fn, sjdt_key_release_fn release_fn) {
    if (set != NULL) {
        if (size < 0) {
            return STARJ_DATA_ERR_NEG_SIZE;
        }
        
        set->data = SJDM_NEW_ARRAY(sjdt_hash_set_node *, size);
        if (set->data != NULL) {
            set->capacity = size;
            set->load_factor = load_factor;
            set->size = 0;

            set->hash_fn = ((hash_fn != NULL) ? hash_fn : sjdf_identity_hash);
            set->comp_fn = ((comp_fn != NULL) ? comp_fn : sjdf_generic_key_comp);
            set->clone_fn = ((clone_fn != NULL) ? clone_fn : sjdf_generic_key_clone);
            set->release_fn = ((release_fn != NULL) ? release_fn : sjdf_generic_key_release);

            return STARJ_DATA_OK;
        } else {
            return STARJ_DATA_ERR_MALLOC;
        }
    }

    return STARJ_DATA_ERR_NULL_PTR;
}

void sjdf_hash_set_release(sjdt_hash_set *set) {
    if (set != NULL) {
        size_t i;

        for (i = 0; i < set->capacity; i++) {
            sjdt_hash_set_node *node;
            sjdt_hash_set_node *tmp;

            for (node = set->data[i]; node != NULL; ) {
                tmp = node->next;
                set->release_fn(node->value);
                free(node);
                node = tmp;
            }
        }
    }
}

int sjdf_hash_set_add(sjdt_hash_set *set, sjdt_key value) {
    size_t index;
    sjdt_hash_set_node *node;

    if (set == NULL) {
        return STARJ_DATA_ERR_NULL_PTR;
    }

    /* Compute the index of the bucket which will hold
     * the value to be added */
    index = set->hash_fn(value) % set->capacity;

    /* Try to locate the value by going through each node in
     * the bucket */
    for (node = set->data[index]; node != NULL; node = node->next) {
        if (!set->comp_fn(node->value, value)) {
            /* The value is already in the set, there is no need to add it */
            return STARJ_DATA_OK;
        }
    }

    node = SJDM_NEW(sjdt_hash_set_node);
    if (node != NULL) {
        node->next = set->data[index];
        node->value = set->clone_fn(value);
        set->data[index] = node;
        set->size += 1;

        if ((set->load_factor > 0.0)
                && (set->size > (set->capacity * set->load_factor))) {
            return sjdf_hash_set_resize(set, 2 * set->capacity + 1);
        }

        return STARJ_DATA_OK;
    } else {
        return STARJ_DATA_ERR_MALLOC;
    }
}

int sjdf_hash_set_remove(sjdt_hash_set *set, const sjdt_key value) {
    size_t index;
    sjdt_hash_set_node *node;
    sjdt_hash_set_node *tmp;

    if (set == NULL) {
        return STARJ_DATA_ERR_NULL_PTR;
    }

    /* Compute the index of the bucket which is expected to hold
     * the value to be removed */
    index = set->hash_fn(value) % set->capacity;

    tmp = NULL;
    for (node = set->data[index]; node != NULL; node = node->next) {
        if (!set->comp_fn(node->value, value)) {
            if (tmp != NULL) {
                tmp->next = node->next;
            } else {
                set->data[index] = node->next;
            }

            set->release_fn(node->value);
            free(node);
            set->size -= 1;
            return STARJ_DATA_OK;
        }
        tmp = node;
    }

    return STARJ_DATA_ERR_NOT_FOUND;
}

int sjdf_hash_set_contains(sjdt_hash_set *set, const sjdt_key value) {
    size_t index;
    sjdt_hash_set_node *node;

    if (set == NULL) {
        return STARJ_DATA_ERR_NULL_PTR;
    }

    index = set->hash_fn(value) % set->capacity;
    for (node = set->data[index]; node != NULL; node = node->next) {
        if (!set->comp_fn(node->value, value)) {
            /* Found a match */
            return STARJ_DATA_OK;
        }
    }

    return STARJ_DATA_ERR_NOT_FOUND;
}

int sjdf_hash_set_resize(sjdt_hash_set *set, size_t new_capacity) {
    sjdt_hash_set_node **new_data;

    if (set == NULL) {
        return STARJ_DATA_ERR_NULL_PTR;
    }
    
    if (new_capacity < 0) {
        return STARJ_DATA_ERR_NEG_SIZE;
    }

    new_data = SJDM_NEW_ARRAY(sjdt_hash_set_node *, new_capacity);

    if (new_data != NULL) {
        size_t i;
        size_t index;
        sjdt_hash_set_node *node;
        sjdt_hash_set_node *tmp;

        for (i = 0; i < set->capacity; i++) {
            for (node = set->data[i]; node != NULL; ) {
                tmp = node->next;

                index = set->hash_fn(node->value) % new_capacity;
                node->next = new_data[index];
                new_data[index] = node;

                node = tmp;
            }
        }

        free(set->data);
        set->data = new_data;
        set->capacity = new_capacity;

        return STARJ_DATA_OK;
    }

    return STARJ_DATA_ERR_MALLOC;
}

int sjdf_hash_set_apply(sjdt_hash_set *set, sjdt_key_fn f) {
    size_t index;
    sjdt_hash_set_node *node;

    if (set == NULL) {
        return STARJ_DATA_ERR_NULL_PTR;
    }

    for (index = 0; index < set->capacity; index++) {
        for (node = set->data[index]; node != NULL; node = node->next) {
            f(node->value);
        }
    }

    return STARJ_DATA_OK;
}
