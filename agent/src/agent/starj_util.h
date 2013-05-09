#ifndef _STARJ_UTIL_H
#define _STARJ_UTIL_H

#include "starj_global.h"

void sjaf_profiler_exit(jint return_val);
void sjaf_split_file();

int sjaf_parse_bool(char *buff, bool *result);
int sjaf_parse_int(char *buff, int *result);
int sjaf_parse_size(char *buff, int *result);

int sjaf_starts_with(const char *str, const char *prefix);
int sjaf_ends_with(const char *str, const char *suffix);
int sjaf_is_char_at(const char *str, unsigned int pos, char c);
int sjaf_replace(char *str, char from, char to);

#endif
