#ifndef _STARJ_DATA_UTIL_H
#define _STARJ_DATA_UTIL_H

#include "starj_data_global.h"

/* Generic functions */
size_t sjdf_identity_hash(const sjdt_key key);

int sjdf_generic_key_comp(const sjdt_key key1, const sjdt_key key2);
sjdt_key sjdf_generic_key_clone(sjdt_key key);
void sjdf_generic_key_release(sjdt_key key);
void sjdf_generic_key_free(sjdt_key key);

int sjdf_generic_value_comp(const sjdt_value value1, const sjdt_value value2);
sjdt_value sjdf_generic_value_clone(sjdt_value value);
void sjdf_generic_value_release(sjdt_value value);
void sjdf_generic_value_free(sjdt_value value);

/* String functions */

size_t sjdf_string_hash(const sjdt_key key);

sjdt_key sjdf_string_key_clone(sjdt_key key);
int sjdf_string_key_comp(const sjdt_key key1, const sjdt_key key2);
void sjdf_string_key_release(sjdt_key key);

sjdt_value sjdf_string_value_clone(sjdt_value value);
int sjdf_string_value_comp(const sjdt_value value1, const sjdt_value value2);
void sjdf_string_value_release(sjdt_value value);
 

#endif
