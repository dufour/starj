#include "starj_data_util.h"
#include <string.h>

/* Generic functions */

size_t sjdf_identity_hash(const sjdt_key key) {
    return ((size_t) key);
}

int sjdf_generic_key_comp(const sjdt_key key1, const sjdt_key key2) {
    return (int) (key2 - key1);
}

sjdt_key sjdf_generic_key_clone(sjdt_key key) {
    return key;
}

void sjdf_generic_key_release(sjdt_key key) {

}

void sjdf_generic_key_free(sjdt_key key) {
    void *p = (void *) key;

    if (p == NULL) {
        free(p);
    }
}

int sjdf_generic_value_comp(const sjdt_value value1, const sjdt_value value2) {
    return (int) (value2 - value1);
}

sjdt_value sjdf_generic_value_clone(sjdt_value value) {
    return value;
}

void sjdf_generic_value_release(sjdt_value value) {

}

void sjdf_generic_value_free(sjdt_value value) {
    void *p = (void *) value;

    if (p == NULL) {
        free(p);
    }
}

/* String functions */

size_t sjdf_string_hash(const sjdt_key key) {
    size_t hash = 0;
    char *s = (char *) key;

    if (s != NULL) {
        while (*s) {
            hash = (hash << 1) + *s++;
        }
    }

    return hash;
}

sjdt_key sjdf_string_key_clone(sjdt_key key) {
    char *s;

    s = (char *) key;
    return strdup(s);
}

int sjdf_string_key_comp(const sjdt_key key1, const sjdt_key key2) {
    char *s1;
    char *s2;

    s1 = (char *) key1;
    s2 = (char *) key2;

    return strcmp(s1, s2);
}

void sjdf_string_key_release(sjdt_key key) {
    char *s;

    s = (char *) key;
    if (s != NULL) {
        free(s);
    }
}

sjdt_value sjdf_string_value_clone(sjdt_value value) {
    char *s;

    s = (char *) value;
    return strdup(s);
}

int sjdf_string_value_comp(const sjdt_value value1, const sjdt_value value2) {
    char *s1;
    char *s2;

    s1 = (char *) value1;
    s2 = (char *) value2;

    return strcmp(s1, s2);
}

void sjdf_string_value_release(sjdt_value value) {
    char *s;

    s = (char *) value;
    if (s != NULL) {
        free(s);
    }
}
