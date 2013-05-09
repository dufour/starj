#ifndef _CFREADER_FIELDS_H
#define _CFREADER_FIELDS_H

typedef struct field_info cfrt_field_info;

#include "cfreader_global.h"
#include "cfreader_attributes.h"
#include "cfreader_file.h"

struct field_info {
    u2 access_flags;
    u2 name_index;
    u2 descriptor_index;
    u2 attributes_count;
    cfrt_attribute_info *attributes;
};

#include "cfreader.h"
#include "cfreader_constant_pool.h"

#ifdef CFREADER_DEBUG_MODE
    void printFields(cfrt_classfile *c);
#endif

int cfrf_parse_fields(cfrt_classfile *c, cfrt_file *f);
int cfrf_parse_field_info(cfrt_field_info *method, cfrt_cp_info *cp, cfrt_file *f);

#endif
