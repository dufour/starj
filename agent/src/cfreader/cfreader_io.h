#ifndef _CFREADER_IO_H
#define _CFREADER_IO_H

#include "cfreader_global.h"
#include "cfreader_file.h"

int cfrf_read_u1(u1 *v, cfrt_file *f);
int cfrf_read_u2(u2 *v, cfrt_file *f);
int cfrf_read_u4(u4 *v, cfrt_file *f);
int cfrf_read_u1_table(u1 *t, u4 length, cfrt_file *f);
int cfrf_read_u2_table(u2 *t, u4 length, cfrt_file *f);
int cfrf_read_u4_table(u4 *t, u4 length, cfrt_file *f);

#endif
