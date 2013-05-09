#include "cfreader_fields.h"
#include "cfreader_io.h"

#ifdef CFREADER_DEBUG_MODE
    #include <stdio.h>

void printFieldInfo(cfrt_field_info *field, FILE *f) {
    fprintf(f, "        access_flags: 0x%X\n", field->access_flags);
    fprintf(f, "        name_index: %d\n", field->name_index);
    fprintf(f, "        descriptor_index: %d\n", field->descriptor_index);
    fprintf(f, "        attributes:\n");
    printAttributes(field->attributes, field->attributes_count, "        ");
}

void printFields(cfrt_classfile *c) {
    int i;

    fprintf(stderr, "    Field count: %d\n", c->fields_count);
    for (i = 0; i < c->fields_count; i++) {
        fprintf(stderr, "    Field #%d:\n", i);
        printFieldInfo(c->fields + i, stderr);
    }
}
#endif

int cfrf_parse_fields(cfrt_classfile *c, cfrt_file *f) {
    u2 fields_count;
    cfrt_field_info *fields;
    u2 i;
    int result;

    if (c == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&fields_count, f);
    if (result != CFREADER_OK) {
        return result;
    }

    if (fields_count > 0) {
        fields = CFRM_NEW_ARRAY(cfrt_field_info, fields_count);
        if (fields == NULL) {
            return CFREADER_ERR_MALLOC;
        }

        for (i = 0; i < fields_count; i++) {
            result = cfrf_parse_field_info(fields + i, c->constant_pool, f);
            if (result != CFREADER_OK) {
                free(fields);
                return result;
            }
        }
    } else {
        fields = NULL;
    } 
 
    c->fields_count = fields_count;
    c->fields = fields;

#ifdef CFREADER_DEBUG_MODE
    printFields(c);
#endif
    
    return CFREADER_OK;
}

int cfrf_parse_field_info(cfrt_field_info *field, cfrt_cp_info *cp, cfrt_file *f) {
    int result;
    u2 access_flags;
    u2 name_index;
    u2 descriptor_index;
    u2 attributes_count;
    cfrt_attribute_info *attributes;
    
    if (field == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&access_flags, f);
    if (result != CFREADER_OK) {
        return result;
    }
    
    result = cfrf_read_u2(&name_index, f);
    if (result != CFREADER_OK) {
        return result;
    }
    
    result = cfrf_read_u2(&descriptor_index, f);
    if (result != CFREADER_OK) {
        return result;
    }
    
    result = cfrf_read_u2(&attributes_count, f);
    if (result != CFREADER_OK) {
        return result;
    }

    result = cfrf_parse_attributes(&attributes, attributes_count, cp, f);
    if (result != CFREADER_OK) {
        return result;
    }

    field->access_flags = access_flags;
    field->name_index = name_index;
    field->descriptor_index = descriptor_index;
    field->attributes_count = attributes_count;
    field->attributes = attributes;

    return CFREADER_OK;
}
