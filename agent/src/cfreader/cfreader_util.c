#include "cfreader_util.h"

#include <string.h>

int cfrf_starts_with(const char *str, const char *prefix) {
    int prefix_len, str_len;
    if ((prefix == NULL) || (str == NULL)) {
        return 0;
    }

    prefix_len = strlen(prefix);
    if (prefix_len == 0) {
        return 1; /* Java API compliance */
    }
    str_len = strlen(str);

    if (prefix_len > str_len) {
        return 0;
    }

    return (strncmp(str, prefix, prefix_len) == 0);
}


int cfrf_ends_with(const char *str, const char *suffix) {
    int suffix_len, str_len;
    
    if ((suffix == NULL) || (str == NULL)) {
        return 0;
    }

    suffix_len = strlen(suffix);
    if (suffix_len == 0) {
        return 1; // Java API compliance
    }
    str_len = strlen(str);

    if (suffix_len > str_len) {
        return 0;
    }

    return (strcmp(str + (str_len - suffix_len), suffix) == 0);
}


int cfrf_is_char_at(const char *str, unsigned int pos, char c) {
    if (pos >= strlen(str)) {
        return 0;
    }

    return (str[pos] == c);
}

void cfrf_replace(char *str, char from, char to) {
    for (; *str; str++) {
        if (*str == from) {
            *str = to;
        }
    }
}

u2 cfrf_calculate_UTF8_char_length(u1 *bytes, u2 length) {
    u2 i;
    u2 result = 0; 
    u1 x;

    for (i = 0; i < length; result++) {
        x = bytes[i];

        if (!(x & (u1)(0x80))) {
            /* x is of the form 0xxxxxx */
            i += 1;
        } else if (((x & (u1)(0xe0)) == (u1)(0xc0))) {
            i += 2;
        } else {
            i += 3;
        }
    }

    return result;
}



u2 cfrf_get_next_UTF8_char(u1 *bytes, u2 *position) {
    u1 x, y, z;

    x = bytes[(*position)++];
    if (!(x & (u1)(0x80))) {
        return (u2)(x);
    } else if (((x & (u1)(0xe0)) == (u1)(0xc0))) {
        y = bytes[(*position)++];
        return (u2)(((x & 0x1f) << 6) | (y & 0x3f));
    } else {
        y = bytes[(*position)++];
        z = bytes[(*position)++];
        return (u2)(((x & 0xf) << 12) | ((y & 0x3f) << 6) | (z & 0x3f));
    }
}

int cfrf_UTF8_to_string(u1 *bytes, u2 length, char *str, int str_len) {
    u2 i;
    u2 position = 0;
    u2 char_length;
    
    if (str == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    char_length = cfrf_calculate_UTF8_char_length(bytes, length);
    if (str_len < char_length + 1) {
        return CFREADER_ERR_BUFFER_SIZE;
    }
    
    for (i = (u2)(0); i < char_length; i++) {
        /* Check for valid ASCII */
        u2 c;
        c = cfrf_get_next_UTF8_char(bytes, &position);
        if (c > 127) {
            return CFREADER_ERR_NOT_ASCII;
        }

        str[i] = (char) c;
    }

    str[i] = '\0';

    return CFREADER_OK;
}
