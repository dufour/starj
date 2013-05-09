#ifndef _CFREADER_FILE_H
#define _CFREADER_FILE_H

#include <stdio.h>
#include "cfreader_global.h"
#include "unzip.h"

#define CFREADER_REGULAR_FILE ((u1) 0)
#define CFREADER_ARCHIVE_FILE ((u1) 1)

typedef struct cfreader_file {
    u1 type;
    union {
        unzFile archive;
        FILE *regular;
    } file;
} cfrt_file;

cfrt_file *cfrf_fopen_regular(const char *filename, const char *mode);
cfrt_file *cfrf_fopen_archive(const char *filename, const char *zipEntry);
cfrt_file *cfrf_fopen_archive_location(const char *filename, const unz_file_pos *pos);
void cfrf_fclose(cfrt_file *f);

#endif
