#ifndef _STARJ_SPEC_READER_H
#define _STARJ_SPEC_READER_H

#include <stdio.h>
#include "starj_global.h"
#include "starj_io.h"

/* Note: The magic number spells out Sable SES,
 * for Sable StarJ Event Specification */
#define STARJ_SPEC_MAGIC 0x5AB1E5E5
/* #define STARJ_SPEC_MAGIC "AES" */
#define STARJ_SPEC_MAJOR_VERSION ((unsigned char) 1)
#define STARJ_SPEC_MINOR_VERSION ((unsigned char) 0)

/* ========================================================================= *
 *                                 Bit Masks                                 *
 * ========================================================================= */

/* Common Fields */
#define STARJ_FIELD_RECORDED               ((sjat_field_mask) 0x00000001)
#define STARJ_FIELD_COUNTED                ((sjat_field_mask) 0x00000002)
#define STARJ_FIELD_ENV_ID                 ((sjat_field_mask) 0x00000004)
#define STARJ_FIELD_REQUIRED               ((sjat_field_mask) 0x00008000)


/* Arena Delete/New  Event */
#define STARJ_FIELD_ARENA_ID               ((sjat_field_mask) 0x00000008)
#define STARJ_FIELD_ARENA_NAME             ((sjat_field_mask) 0x00000010)

/* Class Load Event */
#define STARJ_FIELD_CLASS_NAME             ((sjat_field_mask) 0x00000008)
#define STARJ_FIELD_SOURCE_NAME            ((sjat_field_mask) 0x00000010)
#define STARJ_FIELD_NUM_INTERFACES         ((sjat_field_mask) 0x00000020)
#define STARJ_FIELD_NUM_METHODS            ((sjat_field_mask) 0x00000040)
#define STARJ_FIELD_METHODS                ((sjat_field_mask) 0x00000080)
#define STARJ_FIELD_NUM_STATIC_FIELDS      ((sjat_field_mask) 0x00000100)
#define STARJ_FIELD_STATICS                ((sjat_field_mask) 0x00000200)
#define STARJ_FIELD_NUM_INSTANCE_FIELDS    ((sjat_field_mask) 0x00000400)
#define STARJ_FIELD_INSTANCES              ((sjat_field_mask) 0x00000800)
#define STARJ_FIELD_CLASS_LOAD_CLASS_ID    ((sjat_field_mask) 0x00001000)

/* Class Load Hook Event */

#define STARJ_FIELD_CLASS_DATA_LEN         ((sjat_field_mask) 0x00000008)
#define STARJ_FIELD_CLASS_DATA             ((sjat_field_mask) 0x00000010)

/* Class Unload Event */
#define STARJ_FIELD_CLASS_UNLOAD_CLASS_ID  ((sjat_field_mask) 0x00000008)

/* Compiled Method Load/Unload Event */
#define STARJ_FIELD_METHOD_ID              ((sjat_field_mask) 0x00000008)
#define STARJ_FIELD_CODE_SIZE              ((sjat_field_mask) 0x00000010)
#define STARJ_FIELD_CODE                   ((sjat_field_mask) 0x00000020)
#define STARJ_FIELD_LINENO_TABLE_SIZE      ((sjat_field_mask) 0x00000040)
#define STARJ_FIELD_LINENO_TABLE           ((sjat_field_mask) 0x00000080)

/* Date Dump/Reset Request Event */

/* GC Finish Event */
#define STARJ_FIELD_USED_OBJECTS           ((sjat_field_mask) 0x00000008)
#define STARJ_FIELD_USED_OBJECT_SPACE      ((sjat_field_mask) 0x00000010)
#define STARJ_FIELD_TOTAL_OBJECT_SPACE     ((sjat_field_mask) 0x00000020)

/* GC Start */

/* JNI (Weak) Globalref Alloc/Free */
#define STARJ_FIELD_REF_ID                 ((sjat_field_mask) 0x00000008)
#define STARJ_FIELD_OBJ_ID                 ((sjat_field_mask) 0x00000010)

/* JVM Init Done / Shut Down */

/* Method Entry / Entry 2 */
  /* Method ID defined in Compiled Method Load */
  /* Obj ID defined in JNI Globalref Alloc */

/* Monitor ___ */
#define STARJ_FIELD_OBJECT                 ((sjat_field_mask) 0x00000008)
#define STARJ_FIELD_TIMEOUT                ((sjat_field_mask) 0x00000010)

/* Monitor Dump */
#define STARJ_FIELD_DATA_LEN               ((sjat_field_mask) 0x00000008)
#define STARJ_FIELD_DATA                   ((sjat_field_mask) 0x00000010)
#define STARJ_FIELD_NUM_TRACES             ((sjat_field_mask) 0x00000020)
#define STARJ_FIELD_TRACES                 ((sjat_field_mask) 0x00000040)


/* Object Alloc */
  /* Arena ID defined in Arena Delete */
  /* Obj ID defined in Globalref Alloc */
#define STARJ_FIELD_IS_ARRAY               ((sjat_field_mask) 0x00000020)
#define STARJ_FIELD_SIZE                   ((sjat_field_mask) 0x00000040)
#define STARJ_FIELD_OBJECT_ALLOC_CLASS_ID  ((sjat_field_mask) 0x00000080)

/* Object Dump */
  /* Data Len defined in Montitor Dump */
  /* Data defined in Monitor Dump */

/* Object Free*/
  /* Obj ID defined in JNI Globalref Alloc */

/* Object Move */
  /* Arena ID defined in Arena Delete */
  /* Obj ID defined in JNI Globalref Alloc */
#define STARJ_FIELD_NEW_ARENA_ID           ((sjat_field_mask) 0x00000020)
#define STARJ_FIELD_NEW_OBJ_ID             ((sjat_field_mask) 0x00000040)


/* Raw Monitor ____ */
#define STARJ_FIELD_NAME                   ((sjat_field_mask) 0x00000008)
#define STARJ_FIELD_ID                     ((sjat_field_mask) 0x00000010)

/* Thread End Event */

/* Thread Start Event */
#define STARJ_FIELD_THREAD_NAME            ((sjat_field_mask) 0x00000008)
#define STARJ_FIELD_GROUP_NAME             ((sjat_field_mask) 0x00000010)
#define STARJ_FIELD_PARENT_NAME            ((sjat_field_mask) 0x00000020)
#define STARJ_FIELD_THREAD_ID              ((sjat_field_mask) 0x00000040)
#define STARJ_FIELD_THREAD_ENV_ID          ((sjat_field_mask) 0x00000080)

/* Instruction Start Event */
  /* Method ID defined in Compild Method Load */
#define STARJ_FIELD_OFFSET                 ((sjat_field_mask) 0x00000010)
#define STARJ_FIELD_IS_TRUE                ((sjat_field_mask) 0x00000020)
#define STARJ_FIELD_KEY                    ((sjat_field_mask) 0x00000040)
#define STARJ_FIELD_LOW                    ((sjat_field_mask) 0x00000080)
#define STARJ_FIELD_HI                     ((sjat_field_mask) 0x00000100)
#define STARJ_FIELD_CHOSEN_PAIR_INDEX      ((sjat_field_mask) 0x00000200)
#define STARJ_FIELD_PAIRS_TOTAL            ((sjat_field_mask) 0x00000400)

/* Thread Start Event */
/*#define STARJ_FIELD_NEW_STATUS             ((sjat_field_mask) 0x00000008)*/

/* ========================================================================== * 
 *                          Convenience Definitions                           * 
 * ========================================================================== */

#define STARJ_COUNTED_MASK ((sjat_field_mask) STARJ_FIELD_RECORDED \
                            | STARJ_FIELD_RECORDED)
#define STARJ_FIELD_ALL                    ((sjat_field_mask) 0xFFFFFFFF \
                                            & (~STARJ_FIELD_COUNTED))
#define STARJ_FIELD_ALL_INFO               ((sjat_field_mask) STARJ_FIELD_ALL \
                                            & (~STARJ_FIELD_RECORDED))
/* Macros */
#define SJAM_RECORDED(mask) (mask & STARJ_FIELD_RECORDED)
#define SJAM_COUNTED(mask) (SJAM_RECORDED(mask) && (mask & STARJ_FIELD_COUNTED))

int sjaf_read_spec_file(FILE *f, sjat_field_mask options[], int options_len);

#endif
