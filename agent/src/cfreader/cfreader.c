#include "cfreader.h"
#include "cfreader_util.h"
#include <stdlib.h>
#include <string.h>

#include <stdio.h>

void cfrf_init_classfile(cfrt_classfile *c) {
    if (c!= NULL) {
        c->magic = ((u4) 0);
        c->major_version = ((u2) 0);
        c->minor_version = ((u2) 0);
        c->constant_pool_count = ((u2) 0);
        c->constant_pool = NULL;
        c->access_flags = ((u2) 0);
        c->this_class = ((u2) 0);
        c->super_class = ((u2) 0);
        c->interfaces_count = ((u2) 0);
        c->interfaces = NULL;
        c->fields_count = ((u2) 0);
        c->fields = NULL;
        c->methods_count = ((u2) 0);
        c->methods = NULL;
        c->attributes_count = ((u2) 0);
        c->attributes = NULL;
    }
}

int cfrf_parse_class(cfrt_classfile *c, cfrt_file *f) {
    u4 magic;
    u2 major_version;
    u2 minor_version;
    u2 access_flags;
    u2 this_class;
    u2 super_class;
    int result;
    
    /* Sanity check */
    if (c == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    cfrf_init_classfile(c);

    /* Magic number */
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Reading magic number: ");
#endif
    result = cfrf_read_u4(&magic, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "0x%X\n", magic);
#endif
    
    if (magic != CFREADER_CLASSFILE_MAGIC) {
#ifdef CFREADER_DEBUG_MODE
        fprintf(stderr, ">> Invalid magic number\n");
        result = CFREADER_ERR_MAGIC;
#endif
        goto cleanup;
    }

    /* Minor version */
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Reading minor version: ");
#endif
    result = cfrf_read_u2(&minor_version, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "%d\n", minor_version);
#endif
    
    
    /* Major version */
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Reading major version: ");
#endif
    result = cfrf_read_u2(&major_version, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "%d\n", major_version);
#endif

    /* Constant Pool */
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Reading CP:\n");
#endif
    c->constant_pool_count = 0;
    c->constant_pool = NULL;
    result = cfrf_parse_constant_pool(c, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Done\n");
#endif

    /* Access Flags */
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Reading access flags: ");
#endif
    result = cfrf_read_u2(&access_flags, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "0x%X\n", access_flags);
#endif

    /* This class (CP index) */
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Reading this class index: ");
#endif
    result = cfrf_read_u2(&this_class, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "%d\n", this_class);
#endif

    /* Super class (CP index) */
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Reading super class index: ");
#endif
    result = cfrf_read_u2(&super_class, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "%d\n", super_class);
#endif

    /* Interfaces */
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Reading interfaces:\n");
#endif
    c->interfaces_count = 0;
    c->interfaces = NULL;
    result = cfrf_parse_interfaces(c, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Done\n");
#endif

    /* Fields */
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Reading fields:\n");
#endif
    c->fields_count = 0;
    c->fields = NULL;
    result = cfrf_parse_fields(c, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Done\n");
#endif

    /* Methods */
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Reading methods:\n");
#endif
    c->methods_count = 0;
    c->methods = NULL;
    result = cfrf_parse_methods(c, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Done\n");
#endif

    /* Attributes */
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Reading attributes:\n");
#endif
    c->attributes_count = 0;
    c->attributes = NULL;
    result = cfrf_parse_class_attributes(c, f);
    if (result != CFREADER_OK) {
        goto cleanup;
    }
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Done\n");
#endif

#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Assigning temp values to class file: ");
#endif
    c->magic = magic;
    c->minor_version = minor_version;
    c->major_version = major_version;
    c->access_flags = access_flags;
    c->this_class = this_class;
    c->super_class = super_class;
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "Done\n");
#endif

    return CFREADER_OK;

cleanup:
#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "FAILED\n");
#endif
    cfrf_release_class(c);
    return result;
}

int cfrf_parse_interfaces(cfrt_classfile *c, cfrt_file *f) {
    u2 interfaces_count;
    u2 *interfaces;
    u2 i;
    int result;

    if (c == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }
    
    result = cfrf_read_u2(&interfaces_count, f);
    if (result != CFREADER_OK) {
        return result;
    }

    interfaces = CFRM_NEW_ARRAY(u2, interfaces_count);
    if (interfaces == NULL) {
        return CFREADER_ERR_MALLOC;
    }

    for (i = 0; i < interfaces_count; i++) {
        result= cfrf_read_u2(interfaces + i, f);
        if (result != CFREADER_OK) {
            free(interfaces);
            return result;
        }
    }

    c->interfaces_count = interfaces_count;
    c->interfaces = interfaces;

#ifdef CFREADER_DEBUG_MODE
    fprintf(stderr, "    Interfaces count: %d\n", interfaces_count);
    for (i = 0; i < interfaces_count; i++) {
        fprintf(stderr, "    Interface #%d - %d\n", i, interfaces[i]);
    }
#endif

    return CFREADER_OK;
}

int cfrf_release_attributes(cfrt_attribute_info *attributes, u2 attributes_count) {
    u2 i;

    if (attributes == NULL) {
        return CFREADER_OK;
    }
    
    for (i = 0; i < attributes_count; i++) {
        switch (attributes[i].type) {
            case CFREADER_ATTR_TYPE_CODE:
                cfrf_release_attributes(attributes[i].u.Code.attributes,
                        attributes[i].u.Code.attributes_count);
                if (attributes[i].u.Code.code != NULL) {
                    free(attributes[i].u.Code.code);
                }
                if (attributes[i].u.Code.exception_table != NULL) {
                    free(attributes[i].u.Code.exception_table);
                }
                break;
            case CFREADER_ATTR_TYPE_EXCEPTIONS:
                if (attributes[i].u.Exceptions.exception_index_table != NULL) {
                    free(attributes[i].u.Exceptions.exception_index_table);
                }
                break;
            case CFREADER_ATTR_TYPE_INNER_CLASSES:
                if (attributes[i].u.InnerClasses.classes != NULL) {
                    free(attributes[i].u.InnerClasses.classes);
                }
                break;
            case CFREADER_ATTR_TYPE_LINE_NUMBER_TABLE:
                if (attributes[i].u.LineNumberTable.line_number_table != NULL) {
                    free(attributes[i].u.LineNumberTable.line_number_table);
                }
                break;
            case CFREADER_ATTR_TYPE_LOCAL_VARIABLE_TABLE:
                if (attributes[i].u.LocalVariableTable.local_variable_table != NULL) {
                    free(attributes[i].u.LocalVariableTable.local_variable_table);
                }
                break;
            case CFREADER_ATTR_TYPE_GENERIC:
                if (attributes[i].u.generic.info != NULL) {
                    free(attributes[i].u.generic.info);
                }
        }
    }

    free(attributes);
    return CFREADER_OK;
}

int cfrf_release_class(cfrt_classfile *c) {
    u4 i;
    if (c == NULL) {
        return CFREADER_OK;
    }

    if (c->constant_pool != NULL) {
        free(c->constant_pool);
    }
    if (c->interfaces != NULL) {
        free(c->interfaces);
    }
    if (c->fields != NULL) {
        for (i = 0; i < c->fields_count; i++) {
            cfrf_release_attributes(c->fields[i].attributes,
                    c->fields[i].attributes_count);
        }
        free(c->fields);
    }

    if (c->methods != NULL) {
        for (i = 0; i < c->methods_count; i++) {
            cfrf_release_attributes(c->methods[i].attributes,
                    c->methods[i].attributes_count);
        }
        free(c->methods);
    }

    if (c->attributes != NULL) {
        cfrf_release_attributes(c->attributes, c->attributes_count);
    }
    
    return CFREADER_OK;
}

int cfrf_get_class_name(cfrt_classfile *class, char *s, int len) {
    u2 index = class->this_class;
    cfrt_cp_info *cp = class->constant_pool;
    
    if (index >= class->constant_pool_count) {
        return CFREADER_ERR_INDEX_OUT_OF_BOUNDS;
    }

    if (cp[index].tag == CONSTANT_Class) {
        u2 name_index;
        u2 length;
        u1 *bytes;

        name_index = cp[index].u.CONSTANT_Class_info.name_index;
        if (cp[name_index].tag == CONSTANT_Utf8) {
            int result;
            length = cp[name_index].u.CONSTANT_Utf8_info.length;
            bytes = cp[name_index].u.CONSTANT_Utf8_info.bytes;

            result = cfrf_UTF8_to_string(bytes, length, s, len);

            if (result == CFREADER_OK) {
                cfrf_replace(s, '/', '.');
            }
            return result;
        } 

        return CFREADER_ERR_CONST_NOT_UTF8;
    }

    return CFREADER_ERR_CONST_NOT_CLASS;
}

int cfrf_get_method_by_name(cfrt_classfile *class, char *name, char *signature, cfrt_method_info **method) {
    u2 i;
    int result;
    int err_code;

    if ((class == NULL) || (name == NULL) || (signature == NULL) || (method == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    for (i = 0; i < class->methods_count; i++) {
        u2 name_index;
        u2 signature_index;

        name_index = class->methods[i].name_index;
        signature_index = class->methods[i].descriptor_index;


        result = cfrf_UTF8_constant_cmp(class, name_index, name, &err_code);
        if ((err_code == CFREADER_OK) && (result == 0)) {
            result = cfrf_UTF8_constant_cmp(class, signature_index, signature, &err_code);
            if ((err_code == CFREADER_OK) && (result == 0)) {
                *method = class->methods + i;
                return CFREADER_OK;
            }
        }
//        result = cfrf_get_UTF8_constant(class, name_index, &buff);
//        if (result != CFREADER_OK) {
//            /* buff was unaffected, so do not free it! */
//            continue;
//        }
//
//        if (strcmp(name, buff)) {
//            /* Name mismatch */
//            free(buff);
//            continue;
//        }
//        
//        free(buff);
//
//        result = cfrf_get_UTF8_constant(class, signature_index, &buff);
//        if (result != CFREADER_OK) {
//            continue;
//        }
//
//        if (strcmp(name, buff)) {
//            /* Signature mismatch */
//            free(buff);
//            continue;
//        }
//
//        free(buff);
//        *method = class->methods + i;
//        return CFREADER_OK;
    }

    return CFREADER_ERR_NOT_FOUND;
}
