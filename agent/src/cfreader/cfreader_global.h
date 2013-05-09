#ifndef _CFREADER_GLOBAL_H
#define _CFREADER_GLOBAL_H

#include <stdlib.h>

/* Types */

typedef unsigned char u1;
typedef unsigned short int u2;
typedef unsigned long int u4;

/* Constants */

#define CFREADER_CLASSFILE_MAGIC ((u4) 0xCAFEBABE)

#define ACC_PUBLIC    ((u2) 0x0001)
#define ACC_PRIVATE   ((u2) 0x0002)
#define ACC_PROTECTED ((u2) 0x0004)
#define ACC_STATIC    ((u2) 0x0008)
#define ACC_FINAL     ((u2) 0x0010)
#define ACC_SUPER     ((u2) 0x0020)
#define ACC_VOLATILE  ((u2) 0x0040)
#define ACC_TRANSIENT ((u2) 0x0080)
#define ACC_NATIVE    ((u2) 0x0100)
#define ACC_INTERFACE ((u2) 0x0200)
#define ACC_ABSTRACT  ((u2) 0x0400)
#define ACC_STRICT    ((u2) 0x0800)

/* Errors */

#define CFREADER_OK                        0
#define CFREADER_ERR_IO                   -1
#define CFREADER_ERR_NULL_PTR             -2
#define CFREADER_ERR_MALLOC               -3
#define CFREADER_ERR_NOT_FOUND            -4
#define CFREADER_ERR_MAGIC                -5
#define CFREADER_ERR_MAJ_VERSION          -6
#define CFREADER_ERR_MIN_VERSION          -7
#define CFREADER_ERR_INVALID_CP_TAG       -8
#define CFREADER_ERR_CONST_NOT_UTF8       -9
#define CFREADER_ERR_CONST_NOT_CLASS     -10
#define CFREADER_ERR_INDEX_OUT_OF_BOUNDS -11
#define CFREADER_ERR_BUFFER_SIZE         -12
#define CFREADER_ERR_NOT_ASCII           -13
#define CFREADER_ERR_WRONG_TAG           -14

/* Macros */

#define CFRM_NEW(type) (type *) malloc(sizeof(type))
#define CFRM_NEW_ARRAY(type, size) (type *) calloc(size, sizeof(type))

#endif
