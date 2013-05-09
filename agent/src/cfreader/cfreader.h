#ifndef _CFREADER_H
#define _CFREADER_H

/* Incomplete type */
//struct classfile;
typedef struct classfile cfrt_classfile;

#include "cfreader_global.h"
#include "cfreader_io.h"
#include "cfreader_constant_pool.h"
#include "cfreader_fields.h"
#include "cfreader_methods.h"
#include "cfreader_attributes.h"

struct classfile {
    u4 magic;
    u2 minor_version;
    u2 major_version;
    u2 constant_pool_count;
    cfrt_cp_info *constant_pool;
    u2 access_flags;
    u2 this_class;
    u2 super_class;
    u2 interfaces_count;
    u2 *interfaces;
    u2 fields_count;
    cfrt_field_info *fields;
    u2 methods_count;
    cfrt_method_info *methods;
    u2 attributes_count;
    cfrt_attribute_info *attributes;
};

int cfrf_parse_class(cfrt_classfile *c, cfrt_file *f);
int cfrf_parse_interfaces(cfrt_classfile *c, cfrt_file *f);
int cfrf_release_class(cfrt_classfile *c);
    
int cfrf_get_class_name(cfrt_classfile *class, char *s, int len);
int cfrf_get_method_by_name(cfrt_classfile *class, char *name, char *signature, cfrt_method_info **method);

#endif
