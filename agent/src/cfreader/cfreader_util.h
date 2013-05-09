#ifndef _CFREADER_UTIL_H
#define _CFREADER_UTIL_H

#include "cfreader_global.h"

int cfrf_starts_with(const char *str, const char *prefix);
int cfrf_ends_with(const char *str, const char *suffix);
int cfrf_is_char_at(const char *str, unsigned int pos, char c);
void cfrf_replace(char *str, char from, char to);
u2 cfrf_calculate_UTF8_char_length(u1 *bytes, u2 length);
int cfrf_UTF8_to_string(u1 *bytes, u2 length, char *str, int str_len);

#endif
