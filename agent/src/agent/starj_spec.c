#include "starj_spec.h"

#include <string.h>

int sjaf_read_spec_file(FILE *f, sjat_field_mask masks[], int masks_size) {
    int i;
    jint count;
    jint magic;
    jshort minor_version;
    jshort major_version;

    if (f == NULL) {
        sjaf_error("Invalid spec file pointer: NULL");
        return STARJ_ERR_NULL_PTR;
    }

    if (masks == NULL) {
        sjaf_error("Invalid array pointer: NULL");
        return STARJ_ERR_NULL_PTR;
    }
    
    /* Initialization */
    for (i = 0; i < masks_size; i++) {
        masks[i] = (sjat_field_mask) 0;
    }
    
    /* Read Magic Number */
    if (sjaf_read_jint(&magic, f) != STARJ_OK) {
        sjaf_error("Error reading magic number from spec file");
        return STARJ_ERR_IO;
    }
    if (magic != STARJ_SPEC_MAGIC) {
        sjaf_error("Invalid magic number in spec file");
        return STARJ_ERR_SPEC_MAGIC;
    }

    /* Read Minor Version */
    if (sjaf_read_jshort(&minor_version, f) != STARJ_OK) {
        sjaf_error("Error reading minor version from spec file");
        return STARJ_ERR_IO;
    }
    
    /* Read Major version */
    if (sjaf_read_jshort(&major_version, f) != STARJ_OK) {
        sjaf_error("Error reading major version from spec file");
        return STARJ_ERR_IO;
    }

    if ((minor_version > STARJ_SPEC_MINOR_VERSION)
            || (major_version > STARJ_SPEC_MAJOR_VERSION)) {
        sjaf_error("Unsupported spec version: %d.%d\n", major_version, minor_version);
        return STARJ_ERR_SPEC_VERSION;
    }

    /* Read count */
    if (sjaf_read_jint(&count, f) != STARJ_OK) {
        sjaf_error("Failed to read count from spec file");
        return STARJ_ERR_IO;
    }

    if (count < 0 || count > STARJ_EVENT_COUNT) {
        sjaf_error("Invalid spec file count: %d", count);
        return STARJ_ERR_SPEC_COUNT;
    }
    
    for (i = 0; i < count; i++) {
        byte eventID;
        sjat_field_mask m;
        if ((sjaf_read_byte(&eventID, f) != STARJ_OK)
                || (sjaf_read_field_mask(&m, f) != STARJ_OK)) {
            sjaf_error("Error reading from spec file");
            return STARJ_ERR_IO;
        }
        masks[eventID] = m;
    }

    return STARJ_OK;
}
