#include "cfreader_constant_pool.h"
#include "cfreader_util.h"

#include <string.h>

#ifdef CFREADER_DEBUG_MODE
    #include <stdio.h>

void print_cp_info(cfrt_cp_info *cp, FILE *f) {
    char s[2000];

    if (cp->tag & CONSTANT_Continued_mask) {
        fprintf(f, "(continued)\n");
        return;
    }
    
    switch (cp->tag) {
        case CONSTANT_Class:
            fprintf(f, "CONSTANT_Class\n");
            break;
        case CONSTANT_Fieldref:
            fprintf(f, "CONSTANT_Fieldref\n");
            break;
        case CONSTANT_Methodref:
            fprintf(f, "CONSTANT_Methodref\n");
            break;
        case CONSTANT_InterfaceMethodref:
            fprintf(f, "CONSTANT_InterfaceMethodref\n");
            break;
        case CONSTANT_String:
            fprintf(f, "CONSTANT_String\n");
            break;
        case CONSTANT_Integer:
            fprintf(f, "CONSTANT_Integer\n");
            break;
        case CONSTANT_Float:
            fprintf(f, "CONSTANT_Float\n");
            break;
        case CONSTANT_Long:
            fprintf(f, "CONSTANT_Long\n");
            break;
        case CONSTANT_Double:
            fprintf(f, "CONSTANT_Double\n");
            break;
        case CONSTANT_NameAndType:
            fprintf(f, "CONSTANT_NameAndType\n");
            break;
        case CONSTANT_Utf8:
            strncpy(s, cp->u.CONSTANT_Utf8_info.bytes, cp->u.CONSTANT_Utf8_info.length);
            s[cp->u.CONSTANT_Utf8_info.length] = '\0';
            fprintf(f, "CONSTANT_Utf8: %s\n", s);
            break;
        default:
            fprintf(f, "<Erroneous CP item>\n");
    }
}

void printCP(cfrt_classfile *c) {
    int i;

    for (i = 0; i < c->constant_pool_count; i++) {
        fprintf(stderr, "    %d - ", i);
        print_cp_info(c->constant_pool + i, stderr);
    }
}
#endif

int cfrf_parse_constant_pool(cfrt_classfile *c, cfrt_file *f) {
    u2 i;
    u1 tag;
    u2 count;
    cfrt_cp_info *cp;
    int result;

    if ((c == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }
    
    /* Read the CP item count */
    result = cfrf_read_u2(&count, f);
    if (result != CFREADER_OK) {
        return result;
    }

#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "    CP count: %d\n", count);
#endif

    /* Allocate mem for the CP item array */
    cp = CFRM_NEW_ARRAY(cfrt_cp_info, count);
    if (cp == NULL) {
        return CFREADER_ERR_MALLOC;
    }
    
    /* Read the CP */
    for (i = 1; i < count; i++) {
        result = cfrf_parse_cp_info(cp + i, f);
        if (result != CFREADER_OK) {
            free(cp);
            return result;
        }

        tag = cp[i].tag;
        
        if (tag == CONSTANT_Long || tag == CONSTANT_Double) {
            /* 8-bit constants take up 2 spaces in the CP */
            cp[++i].tag = ((u1) tag | CONSTANT_Continued_mask);
        }
    }

    c->constant_pool_count = count;
    c->constant_pool = cp;

#ifdef CFREADER_DEBUG_MODE
    printCP(c);
#endif
    
    return CFREADER_OK;
}

int cfrf_parse_cp_info(cfrt_cp_info *cp_info, cfrt_file *f) {
    /* Sanity check */
    if ((cp_info == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    if (cfrf_read_u1(&(cp_info->tag), f) != CFREADER_OK) {
        return CFREADER_ERR_IO;
    }

    switch (cp_info->tag) {
        case CONSTANT_Class:
            return cfrf_parse_CONSTANT_Class_info(cp_info, f);
        case CONSTANT_Fieldref:
            return cfrf_parse_CONSTANT_Fieldref_info(cp_info, f);
        case CONSTANT_Methodref:
            return cfrf_parse_CONSTANT_Methodref_info(cp_info, f);
        case CONSTANT_InterfaceMethodref:
            return cfrf_parse_CONSTANT_InterfaceMethodref_info(cp_info, f);
        case CONSTANT_String:
            return cfrf_parse_CONSTANT_String_info(cp_info, f);
        case CONSTANT_Integer:
            return cfrf_parse_CONSTANT_Integer_info(cp_info, f);
        case CONSTANT_Float:
            return cfrf_parse_CONSTANT_Float_info(cp_info, f);
        case CONSTANT_Long:
            return cfrf_parse_CONSTANT_Long_info(cp_info, f);
        case CONSTANT_Double:
            return cfrf_parse_CONSTANT_Double_info(cp_info, f);
        case CONSTANT_NameAndType:
            return cfrf_parse_CONSTANT_NameAndType_info(cp_info, f);
        case CONSTANT_Utf8:
            return cfrf_parse_CONSTANT_Utf8_info(cp_info, f);
        default:
            return CFREADER_ERR_INVALID_CP_TAG;
    }
}

int cfrf_parse_CONSTANT_Class_info(cfrt_cp_info *cp_info, cfrt_file *f) {
    /* Sanity check */
    if ((cp_info == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    return cfrf_read_u2(&(cp_info->u.CONSTANT_Class_info.name_index), f);
}

int cfrf_parse_CONSTANT_Fieldref_info(cfrt_cp_info *cp_info, cfrt_file *f) {
    int result;

    /* Sanity check */
    if ((cp_info == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&(cp_info->u.CONSTANT_Fieldref_info.class_index), f);
    if (result != CFREADER_OK) {
        return result;
    }

    return cfrf_read_u2(&(cp_info->u.CONSTANT_Fieldref_info.name_and_type_index), f);
}

int cfrf_parse_CONSTANT_Methodref_info(cfrt_cp_info *cp_info, cfrt_file *f) {
    int result;
    
    /* Sanity check */
    if ((cp_info == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&(cp_info->u.CONSTANT_Methodref_info.class_index), f);
    if (result != CFREADER_OK) {
        return result;
    }

    return cfrf_read_u2(&(cp_info->u.CONSTANT_Methodref_info.name_and_type_index), f);
}

int cfrf_parse_CONSTANT_InterfaceMethodref_info(cfrt_cp_info *cp_info, cfrt_file *f) {
    int result;
    
    /* Sanity check */
    if ((cp_info == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&(cp_info->u.CONSTANT_InterfaceMethodref_info.class_index), f);
    if (result != CFREADER_OK) {
        return result;
    }

    return cfrf_read_u2(&(cp_info->u.CONSTANT_InterfaceMethodref_info.name_and_type_index), f);
}

int cfrf_parse_CONSTANT_String_info(cfrt_cp_info *cp_info, cfrt_file *f) {
    /* Sanity check */
    if ((cp_info == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    return cfrf_read_u2(&(cp_info->u.CONSTANT_String_info.string_index), f);
}

int cfrf_parse_CONSTANT_Integer_info(cfrt_cp_info *cp_info, cfrt_file *f) {
    /* Sanity check */
    if ((cp_info == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    return cfrf_read_u4(&(cp_info->u.CONSTANT_Integer_info.bytes), f);
}

int cfrf_parse_CONSTANT_Float_info(cfrt_cp_info *cp_info, cfrt_file *f) {
    /* Sanity check */
    if ((cp_info == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    return cfrf_read_u4(&(cp_info->u.CONSTANT_Float_info.bytes), f);
}

int cfrf_parse_CONSTANT_Long_info(cfrt_cp_info *cp_info, cfrt_file *f) {
    int result;
    
    /* Sanity check */
    if ((cp_info == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u4(&(cp_info->u.CONSTANT_Long_info.high_bytes), f);
    if (result != CFREADER_OK) {
        return result;
    }

    return cfrf_read_u4(&(cp_info->u.CONSTANT_Long_info.low_bytes), f);
}

int cfrf_parse_CONSTANT_Double_info(cfrt_cp_info *cp_info, cfrt_file *f) {
    int result;
    
    /* Sanity check */
    if ((cp_info == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u4(&(cp_info->u.CONSTANT_Double_info.high_bytes), f);
    if (result != CFREADER_OK) {
        return result;
    }

    return cfrf_read_u4(&(cp_info->u.CONSTANT_Double_info.low_bytes), f);
}

int cfrf_parse_CONSTANT_NameAndType_info(cfrt_cp_info *cp_info, cfrt_file *f) {
    int result;
    
    /* Sanity check */
    if ((cp_info == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&(cp_info->u.CONSTANT_NameAndType_info.name_index), f);
    if (result != CFREADER_OK) {
        return result;
    }

    return cfrf_read_u2(&(cp_info->u.CONSTANT_NameAndType_info.descriptor_index), f);
}

int cfrf_parse_CONSTANT_Utf8_info(cfrt_cp_info *cp_info, cfrt_file *f) {
    int result;
    u2 length;
    u1 *table;

    /* Sanity check */
    if ((cp_info == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&length,  f);
    if (result != CFREADER_OK) {
        return result;
    }
    
    table = CFRM_NEW_ARRAY(u1, length);
    if (table == NULL) {
        return CFREADER_ERR_MALLOC;
    }
    
    cp_info->u.CONSTANT_Utf8_info.length = length;
    cp_info->u.CONSTANT_Utf8_info.bytes = table;
    return cfrf_read_u1_table(table, (u4) length, f);
}

/* Utility methods */

int cfrf_UTF8_constant_cmp(cfrt_classfile *class, u2 index, const char *buff, int *err_code) {
    cfrt_cp_info *cp;
    u1 *bytes;
    u2 length;
    int result;

    if ((class == NULL) || (buff == NULL)) {
        *err_code = CFREADER_ERR_NULL_PTR;
        return CFREADER_ERR_NULL_PTR;
    }
    
    if ((index <= 0) || (index >= class->constant_pool_count)) {
        *err_code = CFREADER_ERR_INDEX_OUT_OF_BOUNDS;
        return CFREADER_ERR_INDEX_OUT_OF_BOUNDS;
    }
    
    cp = class->constant_pool;

    if (cp[index].tag != CONSTANT_Utf8) {
        *err_code = CFREADER_ERR_WRONG_TAG;
        return CFREADER_ERR_WRONG_TAG;
    }

    bytes = cp[index].u.CONSTANT_Utf8_info.bytes;
    length = cp[index].u.CONSTANT_Utf8_info.length;

    result = strncmp(bytes, buff, length);
    if ((result == 0) && (strlen(buff) > length)) {
        result = 1;
    }

    *err_code = CFREADER_OK;
    return result;
}

int cfrf_get_UTF8_constant(cfrt_classfile *class, u2 index, char **buff) {
    cfrt_cp_info *cp;
    u1 *bytes;
    u2 length;
    u2 buff_size;
    char *s = NULL;
    int result;
    
    /* Sanity check */
    if ((class == NULL) || (buff == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }
    
    if ((index <= 0) || (index >= class->constant_pool_count)) {
        return CFREADER_ERR_INDEX_OUT_OF_BOUNDS;
    }

    cp = class->constant_pool;

    if (cp[index].tag != CONSTANT_Utf8) {
        return CFREADER_ERR_WRONG_TAG;
    }

    bytes = cp[index].u.CONSTANT_Utf8_info.bytes;
    length = cp[index].u.CONSTANT_Utf8_info.length;

    buff_size = cfrf_calculate_UTF8_char_length(bytes, length) + 1;
    
    s = CFRM_NEW_ARRAY(char, buff_size);
    if (s == NULL) {
        return CFREADER_ERR_MALLOC;
    }
    
    result = cfrf_UTF8_to_string(bytes, length, s, buff_size);
    if (result == CFREADER_OK) {
        *buff = s;
    } else {
        free(s);
    }
    
    return result;
}
