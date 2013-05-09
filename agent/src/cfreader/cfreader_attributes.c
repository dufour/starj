#include "cfreader_attributes.h"
#include "cfreader_io.h"
#include <string.h>

#ifdef CFREADER_DEBUG_MODE
    #include <stdio.h>

void printAttributeInfo(cfrt_attribute_info *attr, FILE *f) {
    switch (attr->type) {
        case CFREADER_ATTR_TYPE_GENERIC:
            fprintf(f, "Generic Attrib\n");
            break;
        case CFREADER_ATTR_TYPE_CONSTANT_VALUE:
            fprintf(f, "ConstantValue Attrib\n");
            break;
        case CFREADER_ATTR_TYPE_CODE:
            fprintf(f, "Code Attrib\n");
            break;
        case CFREADER_ATTR_TYPE_EXCEPTIONS:
            fprintf(f, "Exceptions Attrib\n");
            break;
        case CFREADER_ATTR_TYPE_INNER_CLASSES:
            fprintf(f, "InnerClasses Attrib\n");
            break;
        case CFREADER_ATTR_TYPE_SYNTHETIC:
            fprintf(f, "Synthetic Attrib\n");
            break;
        case CFREADER_ATTR_TYPE_SOURCE_FILE:
            fprintf(f, "SourceFile Attrib (%d)\n", attr->u.SourceFile.sourcefile_index);
            break;
        case CFREADER_ATTR_TYPE_LINE_NUMBER_TABLE:
            fprintf(f, "LineNumberTable Attrib\n");
            break;
        case CFREADER_ATTR_TYPE_LOCAL_VARIABLE_TABLE:
            fprintf(f, "LocalVariableTable Attrib\n");
            break;
        case CFREADER_ATTR_TYPE_DEPRECATED:
            fprintf(f, "Deprecated Attrib\n");
            break;
        default:
            fprintf(f, "<<Erroneous attrib>>\n");
    }
}

void printAttributes(cfrt_attribute_info *attr, u2 attr_count, const char *prefix) {
    int i;

    if (prefix == NULL) {
        prefix = "";
    }

    fprintf(stderr, "%s    Attributes count: %d\n", prefix, attr_count);
    for (i = 0; i < attr_count; i++) {
        fprintf(stderr, "%s    Attrib #%d - ", prefix, i);
        printAttributeInfo(attr + i, stderr);
    }
}

#endif

int cfrf_parse_class_attributes(cfrt_classfile *c, cfrt_file *f) {
    u2 attributes_count;
    cfrt_attribute_info *attributes;
    int result;

    if (c == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&attributes_count, f);
    if (result != CFREADER_OK) {
        return result;
    }
    
    result = cfrf_parse_attributes(&attributes, attributes_count, c->constant_pool, f);
    if (result != CFREADER_OK) {
        return result;
    }
 
    c->attributes_count = attributes_count;
    c->attributes = attributes;

#ifdef CFREADER_DEBUG_MODE
    printAttributes(attributes, attributes_count, "");
#endif
    
    return CFREADER_OK;
}

int cfrf_parse_attributes(cfrt_attribute_info **attr, u2 attr_count, cfrt_cp_info *cp, cfrt_file *f) {
    int result;
    u2 i;
    cfrt_attribute_info *p;

    if ((attr == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    if (attr_count == 0) {
        *attr = NULL;
        return CFREADER_OK;
    }

    p = CFRM_NEW_ARRAY(cfrt_attribute_info, attr_count);
    if (p == NULL) {
        return CFREADER_ERR_MALLOC;
    }
    for (i = 0; i < attr_count; i++) {
        result = cfrf_parse_attribute_info(p + i, cp, f);
        if (result != CFREADER_OK) {
            free(p);
            return result;
        }
    }

    *attr = p;

    return CFREADER_OK;
}

int cfrf_parse_attribute_info(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f) {
    int result;
    u2 index;
    u4 length;

    if ((attr == NULL) || (cp == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&index, f);
#ifdef CFREADER_DEBUG_MODE
    
#endif
    if (result != CFREADER_OK) {
        return result;
    }

    result = cfrf_read_u4(&length, f);
    if (result != CFREADER_OK) {
        return result;
    }

    if (cp[index].tag == CONSTANT_Utf8) {
        u1 *bytes = cp[index].u.CONSTANT_Utf8_info.bytes;
        u2 str_length = cp[index].u.CONSTANT_Utf8_info.length;

        attr->attribute_name_index = index;
        attr->attribute_length = length;

        if (!strncmp(CFREADER_ATTR_NAME_CONSTANT_VALUE, bytes, str_length)) {
            return cfrf_parse_constant_value_attr(attr, cp, f);
        }

        if (!strncmp(CFREADER_ATTR_NAME_CODE, bytes, str_length)) {
            return cfrf_parse_code_attr(attr, cp, f);
        }

        if (!strncmp(CFREADER_ATTR_NAME_EXCEPTIONS, bytes, str_length)) {
            return cfrf_parse_exceptions_attr(attr, cp, f);
        }

        if (!strncmp(CFREADER_ATTR_NAME_INNER_CLASSES, bytes, str_length)) {
            return cfrf_parse_inner_classes_attr(attr, cp, f);
        }

        if (!strncmp(CFREADER_ATTR_NAME_SYNTHETIC, bytes, str_length)) {
            return cfrf_parse_synthetic_attr(attr, cp, f);
        }

        if (!strncmp(CFREADER_ATTR_NAME_SOURCE_FILE, bytes, str_length)) {
            return cfrf_parse_source_file_attr(attr, cp, f);
        }

        if (!strncmp(CFREADER_ATTR_NAME_LINE_NUMBER_TABLE, bytes, str_length)) {
            return cfrf_parse_line_number_table_attr(attr, cp, f);
        }

        if (!strncmp(CFREADER_ATTR_NAME_LOCAL_VARIABLE_TABLE, bytes, str_length)) {
            return cfrf_parse_local_variable_table_attr(attr, cp, f);
        }

        if (!strncmp(CFREADER_ATTR_NAME_DEPRECATED, bytes, str_length)) {
            return cfrf_parse_deprecated_attr(attr, cp, f);
        }

        return cfrf_parse_generic_attr(attr, cp, f);
    }

    return CFREADER_ERR_CONST_NOT_UTF8;
}

int cfrf_parse_constant_value_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f) {
    int result;
    u2 constantvalue_index;

    if (attr == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }
    
    result = cfrf_read_u2(&constantvalue_index, f);
    if (result != CFREADER_OK) {
        return result;
    }

    attr->type = CFREADER_ATTR_TYPE_CONSTANT_VALUE;
    attr->u.ConstantValue.constantvalue_index = constantvalue_index;

    return CFREADER_OK;
}

int cfrf_parse_code_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f) {
    int result;
    u2 max_stack;
    u2 max_locals;
    u4 code_length;
    u1 *code = NULL;
    u2 exception_table_length;
    cfrt_exception_table_item *exception_table = NULL;
    u2 attributes_count;
    cfrt_attribute_info *attributes = NULL;

    if ((attr == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    /* Max stack */
    result = cfrf_read_u2(&max_stack, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }

    /* Max locals */
    result = cfrf_read_u2(&max_locals, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }

    /* Code */
    result = cfrf_read_u4(&code_length, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }

    result = cfrf_parse_u1_table(&code, code_length, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }

    /* Exception table */
    result = cfrf_read_u2(&exception_table_length, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }

    if (exception_table_length > 0) {
        u2 i;
        exception_table = CFRM_NEW_ARRAY(cfrt_exception_table_item, exception_table_length);
        if (exception_table == NULL) {
            result = CFREADER_ERR_MALLOC;
            goto cleanup;
        }

        for (i = 0; i < exception_table_length; i++) {
            result = cfrf_parse_exception_table_item(exception_table + i, f);
            if (result != CFREADER_OK) {
                goto cleanup;
            }
        }
    }

    /* Attributes */
    result = cfrf_read_u2(&attributes_count, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }

    result = cfrf_parse_attributes(&attributes, attributes_count, cp, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }

    /* Store results */
    attr->type = CFREADER_ATTR_TYPE_CODE;
    attr->u.Code.max_stack = max_stack;
    attr->u.Code.max_locals = max_locals;
    attr->u.Code.code_length = code_length;
    attr->u.Code.code = code;
    attr->u.Code.exception_table_length = exception_table_length;
    attr->u.Code.exception_table = exception_table;
    attr->u.Code.attributes_count = attributes_count;
    attr->u.Code.attributes = attributes;
    
    return CFREADER_OK;

cleanup:
    if (code != NULL) {
        free(code);
    }

    if (exception_table != NULL) {
        free(exception_table);
    }

    if (attributes != NULL) {
        free(attributes);
    }

    return result;
}

int cfrf_parse_exceptions_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f) {
    int result;
    u2 number_of_exceptions;
    u2 *exception_index_table;

    if (attr == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }
    
    result = cfrf_read_u2(&number_of_exceptions, f);
    if (result != CFREADER_OK) {
        return result;
    }

    result = cfrf_parse_u2_table(&exception_index_table, number_of_exceptions, f);
    if (result != CFREADER_OK) {
        return result;
    }
    
    attr->type = CFREADER_ATTR_TYPE_EXCEPTIONS;
    attr->u.Exceptions.number_of_exceptions = number_of_exceptions;
    attr->u.Exceptions.exception_index_table = exception_index_table;

    return CFREADER_OK;
}

int cfrf_parse_inner_classes_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f) {
    int result;
    u2 i;
    u2 number_of_classes;
    cfrt_class_item *classes;

    if (attr == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }
    
    result = cfrf_read_u2(&number_of_classes, f);
    if (result != CFREADER_OK) {
#ifdef CFREADER_PRINT_RETURNING_INFO
        fprintf(stderr, "Returning from F (%d)\n", result);
#endif
        return result;
    }

    classes = CFRM_NEW_ARRAY(cfrt_class_item, number_of_classes);
    if (classes == NULL) {
        return CFREADER_ERR_MALLOC;
    }

    for (i = 0; i < number_of_classes; i++) {
        result = cfrf_parse_class_item(classes + i, f);
        if (result != CFREADER_OK) {
            free(classes);
#ifdef CFREADER_PRINT_RETURNING_INFO
            fprintf(stderr, "Returning from G (%d)\n", result);
#endif
            return result;
        }
    }

    attr->type = CFREADER_ATTR_TYPE_INNER_CLASSES;
    attr->u.InnerClasses.number_of_classes = number_of_classes;
    attr->u.InnerClasses.classes = classes;

    return CFREADER_OK;
}

int cfrf_parse_synthetic_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f) {
    if (attr == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    attr->type = CFREADER_ATTR_TYPE_SYNTHETIC;

    return CFREADER_OK;
}

int cfrf_parse_source_file_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f) {
    int result;
    u2 sourcefile_index;

    if (attr == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&sourcefile_index, f);
    if (result != CFREADER_OK) {
        return result;
    }

    attr->type = CFREADER_ATTR_TYPE_SOURCE_FILE;
    attr->u.SourceFile.sourcefile_index = sourcefile_index;

    return CFREADER_OK;
}

int cfrf_parse_line_number_table_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f) {
    int result;
    u2 i;
    u2 line_number_table_length;
    cfrt_line_number_table_item *line_number_table;

    if (attr == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&line_number_table_length, f);
    if (result != CFREADER_OK) {
        return result;
    }

    line_number_table = CFRM_NEW_ARRAY(cfrt_line_number_table_item, line_number_table_length);
    if (line_number_table == NULL) {
        return CFREADER_ERR_MALLOC;
    }

    for (i = 0; i < line_number_table_length; i++) {
        result = cfrf_parse_line_number_table_item(line_number_table + i, f);
        if (result != CFREADER_OK) {
            free(line_number_table);
            return result;
        }
    }

    attr->type = CFREADER_ATTR_TYPE_LINE_NUMBER_TABLE;
    attr->u.LineNumberTable.line_number_table_length = line_number_table_length;
    attr->u.LineNumberTable.line_number_table = line_number_table;

    return CFREADER_OK;
}

int cfrf_parse_local_variable_table_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f) {
    int result;
    u2 i;
    u2 local_variable_table_length;
    cfrt_local_variable_table_item *local_variable_table;

    if (attr == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&local_variable_table_length, f);
    if (result != CFREADER_OK) {
        return result;
    }

    local_variable_table = CFRM_NEW_ARRAY(cfrt_local_variable_table_item, local_variable_table_length);
    if (local_variable_table == NULL) {
        return CFREADER_ERR_MALLOC;
    }

    for (i = 0; i < local_variable_table_length; i++) {
        result = cfrf_parse_local_variable_table_item(local_variable_table + i, f);
        if (result != CFREADER_OK) {
            free(local_variable_table);
            return result;
        }
    }

    attr->type = CFREADER_ATTR_TYPE_LOCAL_VARIABLE_TABLE;
    attr->u.LocalVariableTable.local_variable_table_length = local_variable_table_length;
    attr->u.LocalVariableTable.local_variable_table = local_variable_table;

    return CFREADER_OK;
}

int cfrf_parse_deprecated_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f) {
    if (attr == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    attr->type = CFREADER_ATTR_TYPE_DEPRECATED;

    return CFREADER_OK;
}

int cfrf_parse_generic_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f) {
    int result;
    u1 *info;
    
    if (attr == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_parse_u1_table(&info, attr->attribute_length, f);
    if (result != CFREADER_OK) {
        return result;
    }

    attr->type = CFREADER_ATTR_TYPE_GENERIC;
    attr->u.generic.info = info;

    return CFREADER_OK;
}

int cfrf_parse_exception_table_item(cfrt_exception_table_item *item, cfrt_file *f) {
    int result;
    u2 start_pc;
    u2 end_pc;
    u2 handler_pc;
    u2 catch_type;

    if (item == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&start_pc, f);
    if (result != CFREADER_OK) {
        return result;
    }

    result = cfrf_read_u2(&end_pc, f);
    if (result != CFREADER_OK) {
        return result;
    }

    result = cfrf_read_u2(&handler_pc, f);
    if (result != CFREADER_OK) {
        return result;
    }
    
    result = cfrf_read_u2(&catch_type, f);
    if (result != CFREADER_OK) {
        return result;
    }

    item->start_pc = start_pc;
    item->end_pc = end_pc;
    item->handler_pc = handler_pc;
    item->catch_type = catch_type;

    return CFREADER_OK;
}

int cfrf_parse_class_item(cfrt_class_item *item, cfrt_file *f) {
    int result;
    u2 inner_class_info_index;
    u2 outer_class_info_index;
    u2 inner_name_index;
    u2 inner_class_access_flags;
    
    if (item == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }
    
    result = cfrf_read_u2(&inner_class_info_index, f);
    if (result != CFREADER_OK) {
        return result;
    }
    
    result = cfrf_read_u2(&outer_class_info_index, f);
    if (result != CFREADER_OK) {
        return result;
    }
    
    result = cfrf_read_u2(&inner_name_index, f);
    if (result != CFREADER_OK) {
        return result;
    }
    
    result = cfrf_read_u2(&inner_class_access_flags, f);
    if (result != CFREADER_OK) {
        return result;
    }

    item->inner_class_info_index = inner_class_info_index;
    item->outer_class_info_index = outer_class_info_index;
    item->inner_name_index = inner_name_index;
    item->inner_class_info_index = inner_class_info_index;
    
    return CFREADER_OK;
}

int cfrf_parse_line_number_table_item(cfrt_line_number_table_item *item, cfrt_file *f) {
    int result;
    u2 start_pc;
    u2 line_number;
    
    if (item == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }
    
    result = cfrf_read_u2(&start_pc, f);
    if (result != CFREADER_OK) {
        return result;
    }
    
    result = cfrf_read_u2(&line_number, f);
    if (result != CFREADER_OK) {
        return result;
    }

    item->start_pc = start_pc;
    item->line_number = line_number;

    return CFREADER_OK;
}

int cfrf_parse_local_variable_table_item(cfrt_local_variable_table_item *item, cfrt_file *f) {
    int result;
    u2 start_pc;
    u2 length;
    u2 name_index;
    u2 descriptor_index;
    u2 index;

    if (item == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&start_pc, f);
    if (result != CFREADER_OK) {
        return result;
    }

    result = cfrf_read_u2(&length, f);
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

    result = cfrf_read_u2(&index, f);
    if (result != CFREADER_OK) {
        return result;
    }

    item->start_pc = start_pc;
    item->length = length;
    item->name_index = name_index;
    item->descriptor_index = descriptor_index;
    item->index = index;

    return CFREADER_OK;
}

/* Utility functions */

int cfrf_parse_u1_table(u1 **table, u4 length, cfrt_file *f) {
    int result;
    u1 *p;
    if (length > 0) {
        p = CFRM_NEW_ARRAY(u1, length);
        if (p == NULL) {
            return CFREADER_ERR_MALLOC;
        }
        result = cfrf_read_u1_table(p, length, f);
        if (result != CFREADER_OK) {
            return result;
        }
    } else {
        p = NULL;
    }

    *table = p;

    return CFREADER_OK;
}

int cfrf_parse_u2_table(u2 **table, u4 length, cfrt_file *f) {
    int result;
    u2 *p;

    if (length > 0) {
        p = CFRM_NEW_ARRAY(u2, length);
        if (p == NULL) {
            return CFREADER_ERR_MALLOC;
        }
        result = cfrf_read_u2_table(p, length, f);
        if (result != CFREADER_OK) {
            return result;
        }
    } else {
        p = NULL;
    }

    *table = p;

    return CFREADER_OK;
}
