#ifndef _CFREADER_CONSTANT_POOL_H
#define _CFREADER_CONSTANT_POOL_H

#include "cfreader_global.h"
#include "cfreader_io.h"

/* Special constant that is not specified by the JVM spec 
 * but is used here to indicate that an entry is unused
 * due to a Long or a Double entry taking two cp entries.
 * The constant is ORed with the value of the previous
 * tag so as to indicate which case applies. */
#define CONSTANT_Continued_mask     ((u1) 0x80) 

/* Constants defined in the JVM Spec */
#define CONSTANT_Class              ((u1)  7)
#define CONSTANT_Fieldref           ((u1)  9)
#define CONSTANT_Methodref          ((u1) 10)
#define CONSTANT_InterfaceMethodref ((u1) 11)
#define CONSTANT_String             ((u1)  8)
#define CONSTANT_Integer            ((u1)  3)
#define CONSTANT_Float              ((u1)  4)
#define CONSTANT_Long               ((u1)  5)
#define CONSTANT_Double             ((u1)  6)
#define CONSTANT_NameAndType        ((u1) 12)
#define CONSTANT_Utf8               ((u1)  1)

typedef struct cp_info {
    u1 tag;
    union {
        struct {
            u2 name_index;
        } CONSTANT_Class_info;

        struct {
            u2 class_index;
            u2 name_and_type_index;
        } CONSTANT_Fieldref_info;

        struct {
            u2 class_index;
            u2 name_and_type_index;
        } CONSTANT_Methodref_info;

        struct {
            u2 class_index;
            u2 name_and_type_index;
        } CONSTANT_InterfaceMethodref_info;

        struct {
            u2 string_index;
        } CONSTANT_String_info;

        struct {
            u4 bytes;
        } CONSTANT_Integer_info;

        struct {
            u4 bytes;
        } CONSTANT_Float_info;

        struct {
            u4 high_bytes;
            u4 low_bytes;
        } CONSTANT_Long_info;
        
        struct {
            u4 high_bytes;
            u4 low_bytes;
        } CONSTANT_Double_info;

        struct {
            u2 name_index;
            u2 descriptor_index;
        } CONSTANT_NameAndType_info;

        struct {
            u2 length;
            u1 *bytes;
        } CONSTANT_Utf8_info;
    } u;
} cfrt_cp_info;

#include "cfreader.h"

#ifdef CFREADER_DEBUG_MODE
    void printCP(cfrt_classfile *c);
#endif

int cfrf_parse_constant_pool(cfrt_classfile *c, cfrt_file *f);
int cfrf_parse_cp_info(cfrt_cp_info *cp_info, cfrt_file *f);
int cfrf_parse_CONSTANT_Class_info(cfrt_cp_info *cp_info, cfrt_file *f);
int cfrf_parse_CONSTANT_Fieldref_info(cfrt_cp_info *cp_info, cfrt_file *f);
int cfrf_parse_CONSTANT_Methodref_info(cfrt_cp_info *cp_info, cfrt_file *f);
int cfrf_parse_CONSTANT_InterfaceMethodref_info(cfrt_cp_info *cp_info, cfrt_file *f);
int cfrf_parse_CONSTANT_String_info(cfrt_cp_info *cp_info, cfrt_file *f);
int cfrf_parse_CONSTANT_Integer_info(cfrt_cp_info *cp_info, cfrt_file *f);
int cfrf_parse_CONSTANT_Float_info(cfrt_cp_info *cp_info, cfrt_file *f);
int cfrf_parse_CONSTANT_Long_info(cfrt_cp_info *cp_info, cfrt_file *f);
int cfrf_parse_CONSTANT_Double_info(cfrt_cp_info *cp_info, cfrt_file *f);
int cfrf_parse_CONSTANT_NameAndType_info(cfrt_cp_info *cp_info, cfrt_file *f);
int cfrf_parse_CONSTANT_Utf8_info(cfrt_cp_info *cp_info, cfrt_file *f);

int cfrf_UTF8_constant_cmp(cfrt_classfile *class, u2 index, const char *buff, int *err_code);
int cfrf_get_UTF8_constant(cfrt_classfile *class, u2 index, char **buff);

#endif
