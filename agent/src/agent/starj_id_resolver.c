#include "starj_id_resolver.h"
#include "../data/starj_hash_map.h"
#include "../data/starj_data_util.h"

typedef struct {
    union {
        jint int_id;
        jlong long_id;
    } u;
    bool requested;
} sjat_id_info;

static jint current_thread_id;
static jint current_raw_monitor_id;
static jlong current_arena_id;
static jint current_method_id;
static jlong current_object_id;

/* ---- ID Maps ---- */
/* int -> int maps */
sjdt_hash_map thread_id_map;
sjdt_hash_map raw_monitor_id_map;
sjdt_hash_map method_id_map;
/* int -> long maps */
sjdt_hash_map arena_id_map;
sjdt_hash_map object_id_map;

/* Utility functions */

sjat_id_info *alloc_int_id_info(jint id, bool requested) {
    sjat_id_info *result = SJAM_NEW(sjat_id_info);
    if (result != NULL) {
        result->u.int_id = id;
        result->requested = requested;
    }

    return result;
}

sjat_id_info *alloc_long_id_info(jlong id, bool requested) {
    sjat_id_info *result = SJAM_NEW(sjat_id_info);
    if (result != NULL) {
        result->u.long_id = id;
        result->requested = requested;
    }

    return result;
}

size_t sjaf_jint_key_hash(const sjdt_key key) {
    jint *jint_key = (jint *) key;
    return (size_t) (*jint_key);
}

int sjaf_jint_key_comp(const sjdt_key key1, const sjdt_key key2) {
    jint *jint_key1 = (jint *) key1;
    jint *jint_key2 = (jint *) key2;
    
    return (int)(*jint_key2 - *jint_key1);
}

sjdt_key sjaf_jint_key_clone(sjdt_key key) {
    jint *jint_key = (jint *) key;
    jint *result = SJAM_NEW(jint);
    
    if (result != NULL) {
        *result = *jint_key;
    }
    return result;
}

int sjaf_jint_value_comp(const sjdt_value value1, const sjdt_value value2) {
    jint *jint_value1 = (jint *) value1;
    jint *jint_value2 = (jint *) value2;
    
    return (int)(*jint_value2 - *jint_value1);
}

sjdt_value sjaf_jint_value_clone(sjdt_value value) {
    jint *jint_value = (jint *) value;
    jint *result = SJAM_NEW(jint);
    
    if (result != NULL) {
        *result = *jint_value;
    }
    return result;
}

int sjaf_jlong_value_comp(const sjdt_value value1, const sjdt_value value2) {
    jlong *jlong_value1 = (jlong *) value1;
    jlong *jlong_value2 = (jlong *) value2;
    
    if (*jlong_value2 > *jlong_value1) {
        return 1;
    } else if (*jlong_value2 < *jlong_value1) {
        return -1;
    }
    return 0;
}

sjdt_value sjaf_jlong_value_clone(sjdt_value value) {
    jlong *jlong_value = (jlong *) value;
    jlong *result = SJAM_NEW(jlong);
    
    if (result != NULL) {
        *result = *jlong_value;
    }
    return result;
}

/* Initialization */

void sjaf_id_resolver_init() {
    current_thread_id = 0;
    current_raw_monitor_id = 0;
    current_method_id = 0;
    current_object_id = 0L;
    current_arena_id = 0L;
    
    /* Env ID map (sjat_thread_id -> int) */
    sjdf_hash_map_init(&thread_id_map,
            16,
            0.75,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            sjdf_generic_value_free
    );

    /* Raw Monitor ID map (sjat_raw_monitor_id -> int) */
    sjdf_hash_map_init(&raw_monitor_id_map,
            16,
            0.75,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            sjdf_generic_value_free
    );

    /* Method ID map (sjat_method_id -> int) */
    sjdf_hash_map_init(&method_id_map,
            512,
            0.75,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            sjdf_generic_value_free
    );

    /* Object ID map (sjat_object_id -> long) */
    sjdf_hash_map_init(&object_id_map,
            1024,
            0.75,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            sjdf_generic_value_free
    );

    /* Arena ID map (sjat_arena_id -> long) */
    sjdf_hash_map_init(&arena_id_map,
            16,
            0.75,
            sjaf_jint_key_hash,
            sjaf_jint_key_comp,
            sjaf_jint_key_clone,
            sjdf_generic_key_free,
            NULL,
            NULL,
            sjdf_generic_value_free
    );
}

/* ---- ID Resolution functions ---- */

/* Raw Monitors */
jint sjaf_resolve_raw_monitor_id(sjat_raw_monitor_id id) {
    sjat_id_info *info;
    jint result = 0;
    
    if (id == NULL) {
        return result;
    }
    
    if (sjdf_hash_map_get(&raw_monitor_id_map, id, (sjdt_value *) &info) == STARJ_DATA_OK) {
        if (info != NULL) {
            return info->u.int_id;
        }
    } 
    
    result = ++current_raw_monitor_id;
    info = alloc_int_id_info(result, false);
    if (info != NULL) {
        sjdf_hash_map_put(&raw_monitor_id_map, id, info);
    }
    
    return result;
}

/* Methods */
jint sjaf_resolve_method_id(sjat_method_id id) {
    sjat_id_info *info;
    jint result = 0;
    
    if (id == NULL) {
        return result;
    }
    
    if (sjdf_hash_map_get(&method_id_map, id, (sjdt_value *) &info) == STARJ_DATA_OK) {
        if (info != NULL) {
            return info->u.int_id;
        }
    } 
    
    result = ++current_method_id;
    info = alloc_int_id_info(result, false);
    if (info != NULL) {
        sjdf_hash_map_put(&method_id_map, id, info);
    }
    
    return result;
}

/* Threads */
void sjaf_handle_thread_start(sjat_thread_id id, bool requested) {
   sjat_id_info *info = NULL;
   jint new_id;

   if (sjdf_hash_map_get(&thread_id_map, id, (sjdt_value *) &info) == STARJ_OK) {
       if (requested) {
           return;
       }

       /* This is a real event (not requested) */
       if (info->requested) {
           info->requested = false;
           return;
       }
   }

   /* If we get here, then a new ID has to be issued, since
    * this ID is reused. */
   new_id = ++current_thread_id;
   if (info == NULL) {
      info = alloc_int_id_info(new_id, requested);
      sjdf_hash_map_put(&thread_id_map, id, info);
   } else {
       info->u.int_id = new_id;
       info->requested = requested;
   }
}

void sjaf_handle_thread_end(sjat_thread_id id, bool requested) {
    sjdf_hash_map_remove(&thread_id_map, id);
}

jlong sjaf_resolve_thread_id(sjat_thread_id id) {
    sjat_id_info *info;
    jint result = 0;
    
    if (id == NULL) {
        return result;
    }
    
    if (sjdf_hash_map_get(&thread_id_map, id, (sjdt_value *) &info) == STARJ_DATA_OK) {
        if (info != NULL) {
            return info->u.int_id;
        }
    } 
    
    result = ++current_thread_id;
    info = alloc_int_id_info(result, true);
    if (info != NULL) {
        sjdf_hash_map_put(&thread_id_map, id, info);
    }

    return result;
}

/* Objects */

void sjaf_handle_object_alloc(sjat_object_id id, bool requested) {
    sjat_id_info *info = NULL;
    jlong new_id;

    if (sjdf_hash_map_get(&object_id_map, id, (sjdt_value *) &info) == STARJ_OK) {
        if (requested) {
            return;
        }

        /* This is a real event (not requested) */
        if (info->requested) {
            info->requested = false;
            return;
        }
    }

    /* If we get here, then a new ID has to be issued, since
     * this ID is reused. */
    new_id = ++current_object_id;
    if (info == NULL) {
        info = alloc_long_id_info(new_id, requested);
        sjdf_hash_map_put(&object_id_map, id, info);
    } else {
        info->u.long_id = new_id;
        info->requested = requested;
    }
}

void sjaf_handle_object_move(sjat_object_id from_id, sjat_object_id to_id, bool requested) {
    sjdf_hash_map_move(&object_id_map, from_id, to_id);
}

void sjaf_handle_object_free(sjat_object_id id, bool requested) {
    sjdf_hash_map_remove(&object_id_map, id);
}

jlong sjaf_resolve_object_id(sjat_object_id id) {
    sjat_id_info *info;
    jlong result = 0;
    
    if (id == NULL) {
        fprintf(stderr, "Object ID is: %lld\n", result);
        return result;
    }
    
    if (sjdf_hash_map_get(&object_id_map, id, (sjdt_value *) &info) == STARJ_DATA_OK) {
        if (info != NULL) {
            fprintf(stderr, "Object ID is: %lld\n", info->u.long_id);
            return info->u.long_id;
        }
    } 
    
    result = ++current_object_id;
    info = alloc_long_id_info(result, true);
    if (info != NULL) {
        sjdf_hash_map_put(&object_id_map, id, info);
    }
    
    fprintf(stderr, "Object ID is: %lld\n", result);
    return result;
}

/* Arenas */

void sjaf_handle_arena_new(sjat_arena_id id, bool requested) {
   sjat_id_info *info = NULL;
   jlong new_id;

   if (sjdf_hash_map_get(&object_id_map, &id, (sjdt_value *) &info) == STARJ_OK) {
       if (requested) {
           return;
       }

       /* This is a real event (not requested) */
       if (info->requested) {
           info->requested = false;
           return;
       }
   }

   /* If we get here, then a new ID has to be issued, since
    * this ID is reused. */
   new_id = ++current_arena_id;
   if (info == NULL) {
      info = alloc_long_id_info(new_id, requested);
      sjdf_hash_map_put(&arena_id_map, &id, info);
   } else {
       info->u.long_id = new_id;
       info->requested = requested;
   }
}

void sjaf_handle_arena_delete(sjat_arena_id id, bool requested) {
    sjdf_hash_map_remove(&arena_id_map, &id);
}

jlong sjaf_resolve_arena_id(sjat_arena_id id) {
    sjat_id_info *info;
    jlong result = 0;
    
    if (id == 0) {
        return result;
    }
    
    if (sjdf_hash_map_get(&arena_id_map, &id, (sjdt_value *) &info) == STARJ_DATA_OK) {
        if (info != NULL) {
            return info->u.long_id;
        }
    } 
    
    result = ++current_arena_id;
    info = alloc_long_id_info(result, true);
    if (info != NULL) {
        sjdf_hash_map_put(&arena_id_map, &id, info);
    }

    return result;
}
