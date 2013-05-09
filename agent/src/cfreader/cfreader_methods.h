#ifndef _CCFR_METHODS_H
#define _CCFR_METHODS_H

typedef struct method_info cfrt_method_info;

#include "cfreader_global.h"
#include "cfreader_attributes.h"
#include "cfreader_file.h"

struct method_info {
    u2 access_flags;
    u2 name_index;
    u2 descriptor_index;
    u2 attributes_count;
    cfrt_attribute_info *attributes;
};

#include "cfreader.h"
#include "cfreader_constant_pool.h"

#ifdef CFREADER_DEBUG_MODE
    void printMethods(cfrt_classfile *c);
#endif

int cfrf_parse_methods(cfrt_classfile *c, cfrt_file *f);
int cfrf_parse_method_info(cfrt_method_info *method, cfrt_cp_info *cp, cfrt_file *f);

#endif
