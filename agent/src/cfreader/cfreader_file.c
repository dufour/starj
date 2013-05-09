#include "cfreader_file.h"

cfrt_file *cfrf_fopen_regular(const char *filename, const char *mode) {
    cfrt_file *result;
    FILE *f;

    f = fopen(filename, mode);
    if (f != NULL) {
        result = CFRM_NEW(cfrt_file);
        if (result != NULL) {
            result->type = CFREADER_REGULAR_FILE;
            result->file.regular = f;
            return result;
        } 
        fclose(f);
    }
    
    return NULL;
}

cfrt_file *cfrf_fopen_archive(const char *filename, const char *zipEntry) {
    cfrt_file *result;
    unzFile f;

    f = unzOpen(filename);
    if (f != NULL) {
        if (unzLocateFile(f, zipEntry, 1) == UNZ_OK) {
            if (unzOpenCurrentFile(f) == UNZ_OK) {
                result = CFRM_NEW(cfrt_file);
                if (result != NULL) {
                    result->type = CFREADER_ARCHIVE_FILE;
                    result->file.archive = f;
                    return result;
                } 

                unzCloseCurrentFile(f);
            }
        }
        
        unzClose(f);
    }

    return NULL;
}

cfrt_file *cfrf_fopen_archive_location(const char *filename, const unz_file_pos *pos) {
    cfrt_file *result;
    unzFile f;

    f = unzOpen(filename);
    if (f != NULL) {
        if (unzGoToFilePos(f, (unz_file_pos *) pos) == UNZ_OK) {
            if (unzOpenCurrentFile(f) == UNZ_OK) {
                result = CFRM_NEW(cfrt_file);
                if (result != NULL) {
                    result->type = CFREADER_ARCHIVE_FILE;
                    result->file.archive = f;
                    return result;
                } 

                unzCloseCurrentFile(f);
            }
        }

        unzClose(f);
    }

    return NULL;
}

void cfrf_fclose(cfrt_file *f) {
    if (f != NULL) {
        if (f->type == CFREADER_REGULAR_FILE) {
            fclose(f->file.regular);
        } else if (f->type == CFREADER_ARCHIVE_FILE) {
            unzClose(f->file.archive);
        }
        free(f);
    }
}
