#ifndef _STARJ_DATA_GLOBAL_H
#define _STARJ_DATA_GLOBAL_H

#define SJDM_NEW(type) (type *) malloc(sizeof(type))
#define SJDM_NEW_ARRAY(type, size) (type *) calloc(size, sizeof(type))

#define STARJ_DATA_OK             0
#define STARJ_DATA_ERR_MALLOC    -1
#define STARJ_DATA_ERR_NULL_PTR  -2
#define STARJ_DATA_ERR_NOT_FOUND -3
#define STARJ_DATA_ERR_NEG_SIZE  -4

#include <stdlib.h>


typedef void * sjdt_key;
typedef void * sjdt_value;

typedef size_t (*sjdt_hash_fn)(const sjdt_key);

/* Key functions */
typedef int (*sjdt_key_comp_fn)(const sjdt_key, const sjdt_key);
typedef void (*sjdt_key_release_fn)(sjdt_key);
typedef sjdt_key (*sjdt_key_clone_fn)(sjdt_key);
typedef void (*sjdt_key_fn)(sjdt_key);

/* Value functions */
typedef int (*sjdt_value_comp_fn)(const sjdt_value, const sjdt_value);
typedef void (*sjdt_value_release_fn)(sjdt_value);
typedef sjdt_value (*sjdt_value_clone_fn)(sjdt_value);
typedef void (*sjdt_value_fn)(sjdt_value);

#endif
