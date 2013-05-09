#ifndef _CFREADER_ATTRIBUTES_H
#define _CFREADER_ATTRIBUTES_H

#include "cfreader_global.h"
#include "cfreader_file.h"

#define CFREADER_ATTR_TYPE_GENERIC              ((u1) 0)
#define CFREADER_ATTR_TYPE_CONSTANT_VALUE       ((u1) 1)
#define CFREADER_ATTR_TYPE_CODE                 ((u1) 2)
#define CFREADER_ATTR_TYPE_EXCEPTIONS           ((u1) 3)
#define CFREADER_ATTR_TYPE_INNER_CLASSES        ((u1) 4)
#define CFREADER_ATTR_TYPE_SYNTHETIC            ((u1) 5)
#define CFREADER_ATTR_TYPE_SOURCE_FILE          ((u1) 6)
#define CFREADER_ATTR_TYPE_LINE_NUMBER_TABLE    ((u1) 7)
#define CFREADER_ATTR_TYPE_LOCAL_VARIABLE_TABLE ((u1) 8)
#define CFREADER_ATTR_TYPE_DEPRECATED           ((u1) 9)

#define CFREADER_ATTR_NAME_CONSTANT_VALUE       "ConstantValue"
#define CFREADER_ATTR_NAME_CODE                 "Code"
#define CFREADER_ATTR_NAME_EXCEPTIONS           "Exceptions"
#define CFREADER_ATTR_NAME_INNER_CLASSES        "InnerClasses"
#define CFREADER_ATTR_NAME_SYNTHETIC            "Synthetic"
#define CFREADER_ATTR_NAME_SOURCE_FILE          "SourceFile"
#define CFREADER_ATTR_NAME_LINE_NUMBER_TABLE    "LineNumberTable"
#define CFREADER_ATTR_NAME_LOCAL_VARIABLE_TABLE "LocalVariableTable"
#define CFREADER_ATTR_NAME_DEPRECATED           "Deprecated"

typedef struct exception_table_item {
    u2 start_pc;
    u2 end_pc;
    u2 handler_pc;
    u2 catch_type;
} cfrt_exception_table_item;

typedef struct class_item {
    u2 inner_class_info_index;
    u2 outer_class_info_index;
    u2 inner_name_index;
    u2 inner_class_access_flags;
} cfrt_class_item;

typedef struct line_number_table_item {
    u2 start_pc;
    u2 line_number;
} cfrt_line_number_table_item;

typedef struct local_variable_table_item {
    u2 start_pc;
    u2 length;
    u2 name_index;
    u2 descriptor_index;
    u2 index;
} cfrt_local_variable_table_item;

typedef struct attribute_info {
    u2 attribute_name_index;
    u4 attribute_length;
    u1 type;   /* Not in the Java Class File Spec */
    union {
        struct {
            u2 constantvalue_index;
        } ConstantValue;

        struct {
            u2 max_stack;
            u2 max_locals;
            u4 code_length;
            u1 *code;
            u2 exception_table_length;
            cfrt_exception_table_item *exception_table;
            u2 attributes_count;
            struct attribute_info *attributes;
        } Code;

        struct {
            u2 number_of_exceptions;
            u2 *exception_index_table;
        } Exceptions;

        struct {
            u2 number_of_classes;
            cfrt_class_item *classes;
        } InnerClasses;

        /* Synthetic attribute has no specific information */

        struct {
            u2 sourcefile_index;
        } SourceFile;

        struct {
            u2 line_number_table_length;
            cfrt_line_number_table_item *line_number_table;
        } LineNumberTable;

        struct {
            u2 local_variable_table_length;
            cfrt_local_variable_table_item * local_variable_table;
        } LocalVariableTable;

        /* Deprecated attribute has no specific information */

        struct {
            u1 *info;
        } generic;
    } u;
} cfrt_attribute_info;

#include "cfreader.h"

#ifdef CFREADER_DEBUG_MODE
void printAttributes(cfrt_attribute_info *attr, u2 attr_count, const char *prefix);
#endif

int cfrf_parse_class_attributes(cfrt_classfile *c, cfrt_file *f);
int cfrf_parse_attributes(cfrt_attribute_info **attr, u2 attr_count, cfrt_cp_info *cp, cfrt_file *f);
int cfrf_parse_attribute_info(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f);
int cfrf_parse_constant_value_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f);
int cfrf_parse_code_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f);
int cfrf_parse_exceptions_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f);
int cfrf_parse_inner_classes_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f);
int cfrf_parse_synthetic_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f);
int cfrf_parse_source_file_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f);
int cfrf_parse_line_number_table_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f);
int cfrf_parse_local_variable_table_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f);
int cfrf_parse_deprecated_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f);
int cfrf_parse_generic_attr(cfrt_attribute_info *attr, cfrt_cp_info *cp, cfrt_file *f);

int cfrf_parse_exception_table_item(cfrt_exception_table_item *item, cfrt_file *f);
int cfrf_parse_class_item(cfrt_class_item *item, cfrt_file *f);
int cfrf_parse_line_number_table_item(cfrt_line_number_table_item *item, cfrt_file *f);
int cfrf_parse_local_variable_table_item(cfrt_local_variable_table_item *item, cfrt_file *f);
int cfrf_parse_u1_table(u1 **table, u4 length, cfrt_file *f);
int cfrf_parse_u2_table(u2 **table, u4 length, cfrt_file *f);

#endif
