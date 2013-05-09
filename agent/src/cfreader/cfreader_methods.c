#include "cfreader_methods.h"
#include "cfreader_io.h"

#ifdef CFREADER_DEBUG_MODE
    #include <stdio.h>

void printMethodInfo(cfrt_method_info *method, FILE *f) {
    fprintf(f, "        access_flags: 0x%X\n", method->access_flags);
    fprintf(f, "        name_index: %d\n", method->name_index);
    fprintf(f, "        descriptor_index: %d\n", method->descriptor_index);
    fprintf(f, "        attributes:\n");
    printAttributes(method->attributes, method->attributes_count, "        ");
}

void printMethods(cfrt_classfile *c) {
    int i;

    fprintf(stderr, "    Method count: %d\n", c->methods_count);
    for (i = 0; i < c->methods_count; i++) {
        fprintf(stderr, "    Method #%d:\n", i);
        printMethodInfo(c->methods + i, stderr);
    }
}
#endif

int cfrf_parse_methods(cfrt_classfile *c, cfrt_file *f) {
    u2 methods_count;
    cfrt_method_info *methods;
    u2 i;
    int result;

    if (c == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    result = cfrf_read_u2(&methods_count, f);
    if (result != CFREADER_OK) {
        return result;
    }

    if (methods_count > 0) {
        methods = CFRM_NEW_ARRAY(cfrt_method_info, methods_count);
        if (methods == NULL) {
            return CFREADER_ERR_MALLOC;
        }

        for (i = 0; i < methods_count; i++) {
            result = cfrf_parse_method_info(methods + i, c->constant_pool, f);
            if (result != CFREADER_OK) {
                free(methods);
                return result;
            }
        }
    } else {
        methods = NULL;
    } 
 
    c->methods_count = methods_count;
    c->methods = methods;

#ifdef CFREADER_DEBUG_MODE
    printMethods(c);
#endif
    
    return CFREADER_OK;
}

int cfrf_parse_method_info(cfrt_method_info *method, cfrt_cp_info *cp, cfrt_file *f) {
    int result;
    u2 access_flags;
    u2 name_index;
    u2 descriptor_index;
    u2 attributes_count;
    cfrt_attribute_info *attributes;

    if (method == NULL) {
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

    method->access_flags = access_flags;
    method->name_index = name_index;
    method->descriptor_index = descriptor_index;
    method->attributes_count = attributes_count;
    method->attributes = attributes;

    return CFREADER_OK;
}
