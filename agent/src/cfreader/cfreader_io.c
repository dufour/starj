#include "cfreader_io.h"

/* Input functions */

int cfrf_read_u1(u1 *v, cfrt_file *f) {
    size_t result = 0;

    if ((v == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    if (f->type == CFREADER_REGULAR_FILE) {
        result = fread(v, 1, 1, f->file.regular);
    } else if (f->type == CFREADER_ARCHIVE_FILE) {
        result = unzReadCurrentFile(f->file.archive, v, 1);
    }

    if (result == 1) {
        return CFREADER_OK;
    }

    return CFREADER_ERR_IO;
}

int cfrf_read_u2(u2 *v, cfrt_file *f) {
    size_t result = 0;
    u1 b[2];

    if ((v == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }
    
    if (f->type == CFREADER_REGULAR_FILE) {
        result = fread(b, 1, 2, f->file.regular);
    } else if (f->type == CFREADER_ARCHIVE_FILE) {
        result = unzReadCurrentFile(f->file.archive, b, 2);
    }
    
    if (result == 2) {
        *v = ((u2)b[0] << 8) | (u2)b[1]; 
        return CFREADER_OK;
    }

    return CFREADER_ERR_IO;
}

int cfrf_read_u4(u4 *v, cfrt_file *f) {
    size_t result = 0;
    u1 b[4];

    if ((v == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }
    
    if (f->type == CFREADER_REGULAR_FILE) {
        result = fread(b, 1, 4, f->file.regular);
    } else if (f->type == CFREADER_ARCHIVE_FILE) {
        result = unzReadCurrentFile(f->file.archive, b, 4);
    }
    
    if (result == 4) {
        int i;

        *v = (u4) 0;
        for (i = 0; i < 4; i++) {
            *v |= ((u4)b[i]) << ((3 - i) * 8);
        }
        return CFREADER_OK;
    }

    return CFREADER_ERR_IO;
}

int cfrf_read_u1_table(u1 *t, u4 length, cfrt_file *f) {
    size_t result = 0;

    if ((t == NULL) || (f == NULL)) {
        return CFREADER_ERR_NULL_PTR;
    }

    if (f->type == CFREADER_REGULAR_FILE) {
        result = fread(t, 1, length, f->file.regular);
    } else if (f->type == CFREADER_ARCHIVE_FILE) {
        result = unzReadCurrentFile(f->file.archive, t, length);
    }

    if (result == length) {
        return CFREADER_OK;
    }

    return CFREADER_ERR_IO;
}

int cfrf_read_u2_table(u2 *t, u4 length, cfrt_file *f) {
    u4 i;
    int result;

    if (t == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    for (i = 0; i < length; i++) {
        result = cfrf_read_u2(t + i, f);
        if (result != CFREADER_OK) {
            return result;
        }
    }

    return CFREADER_OK;
}

int cfrf_read_u4_table(u4 *t, u4 length, cfrt_file *f) {
    u4 i;
    int result;

    if (t == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    for (i = 0; i < length; i++) {
        result = cfrf_read_u4(t + i, f);
        if (result != CFREADER_OK) {
            return result;
        }
    }

    return CFREADER_OK;
}

/* Output functions */
