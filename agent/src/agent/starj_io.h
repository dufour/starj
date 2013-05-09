#ifndef _STARJ_IO_H
#define _STARJ_IO_H

#include <stdio.h>
#include "starj_global.h"

#define STARJ_IO_BUFFER_SIZE 2048

#define SJAM_NULL_STR "(null)"

void sjaf_debug(char *format, ...);
void sjaf_extra(char *format, ...);
void sjaf_message(char *format, ...);
void sjaf_warning(char *format, ...);
void sjaf_error(char *format, ...);

int sjaf_read_byte(byte *b, void *f);
int sjaf_read_jshort(jshort *s, void *f);
int sjaf_read_jint(jint *i, void *f);
int sjaf_read_jlong(jlong *l, void *f);
int sjaf_read_event(sjat_event *event, void *f);
int sjaf_read_field_mask(sjat_field_mask *m, void *f);
int sjaf_read_bytes(byte *s, jint count, void *f);
int sjaf_read_thread_id(sjat_thread_id *t, void *f);
int sjaf_read_object_id(sjat_object_id *obj, void *f);
int sjaf_read_class_id(sjat_class_id *c, void *f);
int sjaf_read_method_id(sjat_method_id *m, void *f);
int sjaf_read_arena_id(sjat_arena_id *a, void *f);
int sjaf_read_jni_ref_id(sjat_jni_ref_id *j, void *f);
int sjaf_read_raw_monitor_id(sjat_raw_monitor_id *m, void *f);
int sjaf_read_jboolean(jboolean *b, void *f);

int sjaf_write_byte(byte b, void *f);
int sjaf_write_jshort(jshort s, void *f);
int sjaf_write_jint(jint v, void *f);
int sjaf_write_jlong(jlong l, void *f);
int sjaf_write_bytes(const byte *s, jint count, void *f);
int sjaf_write_event(sjat_event event, void *f);
int sjaf_write_field_mask(sjat_field_mask m, void *f);
int sjaf_write_thread_id(sjat_thread_id t, void *f);
int sjaf_write_object_id(sjat_object_id obj, void *f);
int sjaf_write_class_id(sjat_class_id c, void *f);
int sjaf_write_method_id(sjat_method_id m, void *f);
int sjaf_write_arena_id(sjat_arena_id a, void *f);
int sjaf_write_jni_ref_id(sjat_jni_ref_id j, void *f);
int sjaf_write_raw_monitor_id(sjat_raw_monitor_id m, void *f);
int sjaf_write_utf8(const char *s, void *f);
int sjaf_write_inst_offset(jint offset, void *f);
int sjaf_write_jboolean(jboolean b, void *f);
int sjaf_write_jboolean_attrib(const char *name, jboolean value, void *f);
int sjaf_write_jint_attrib(const char *name, jint value, void *f);
int sjaf_write_utf8_attrib(const char *name, const char *value, void *f);

int sjaf_flush_buffer(void *f);
int sjaf_buffered_write_byte(byte b, void *f);
int sjaf_buffered_write_jshort(jshort s, void *f);
int sjaf_buffered_write_jint(jint v, void *f);
int sjaf_buffered_write_jlong(jlong l, void *f);
int sjaf_buffered_write_bytes(const byte *s, jint count, void *f);
int sjaf_buffered_write_event(sjat_event event, void *f);
int sjaf_buffered_write_field_mask(sjat_field_mask m, void *f);
int sjaf_buffered_write_thread_id(sjat_thread_id t, void *f);
int sjaf_buffered_write_object_id(sjat_object_id obj, void *f);
int sjaf_buffered_write_class_id(sjat_class_id c, void *f);
int sjaf_buffered_write_method_id(sjat_method_id m, void *f);
int sjaf_buffered_write_arena_id(sjat_arena_id a, void *f);
int sjaf_buffered_write_jni_ref_id(sjat_jni_ref_id j, void *f);
int sjaf_buffered_write_raw_monitor_id(sjat_raw_monitor_id m, void *f);
int sjaf_buffered_write_utf8(const char *s, void *f);
int sjaf_buffered_write_inst_offset(jint offset, void *f);
int sjaf_buffered_write_jboolean(jboolean b, void *f);
int sjaf_buffered_write_jboolean_attrib(const char *name, jboolean value,
        void *f);
int sjaf_buffered_write_jint_attrib(const char *name, jint value, void *f);
int sjaf_buffered_write_utf8_attrib(const char *name, const char *value,
        void *f);

#ifdef STARJ_BUFFERED_TRACE_IO
    #define SJAM_FLUSH_BUFFER(f)            sjaf_flush_buffer(f)
    #define SJAM_WRITE_BYTE(v, f)           sjaf_buffered_write_byte(v, f)
    #define SJAM_WRITE_JSHORT(v, f)         sjaf_buffered_write_jshort(v, f)
    #define SJAM_WRITE_JINT(v, f)           sjaf_buffered_write_jint(v, f)
    #define SJAM_WRITE_JLONG(v, f)          sjaf_buffered_write_jlong(v, f)
    #define SJAM_WRITE_BYTES(v, n, f)       sjaf_buffered_write_bytes(v, n, f)
    #define SJAM_WRITE_EVENT(v, f)          sjaf_buffered_write_event(v, f)
    #define SJAM_WRITE_FIELD_MASK(v, f)     sjaf_buffered_write_field_mask(v, f)
    #define SJAM_WRITE_THREAD_ID(v, f)      sjaf_buffered_write_thread_id(v, f)
    #define SJAM_WRITE_OBJECT_ID(v, f)      sjaf_buffered_write_object_id(v, f)
    #define SJAM_WRITE_CLASS_ID(v, f)       sjaf_buffered_write_object_id(v, f)
    #define SJAM_WRITE_METHOD_ID(v, f)      sjaf_buffered_write_method_id(v, f)
    #define SJAM_WRITE_ARENA_ID(v, f)       sjaf_buffered_write_arena_id(v, f)
    #define SJAM_WRITE_JNI_REF_ID(v, f)     sjaf_buffered_write_jni_ref_id(v, f)
    #define SJAM_WRITE_RAW_MONITOR_ID(v, f) sjaf_buffered_write_raw_monitor_id(v, f)
    #define SJAM_WRITE_UTF8(v, f)           sjaf_buffered_write_utf8(v, f)
    #define SJAM_WRITE_INST_OFFSET(v, f)    sjaf_buffered_write_inst_offset(v, f)
    #define SJAM_WRITE_JBOOLEAN(v, f)       sjaf_buffered_write_jboolean(v, f)
    #define SJAM_WRITE_JBOOLEAN_ATTRIB(n, v, f) sjaf_buffered_write_jboolean(n, v, f)
    #define SJAM_WRITE_JINT_ATTRIB(n, v, f) sjaf_buffered_write_jint_attrib(n, v, f)
    #define SJAM_WRITE_UTF8_ATTRIB(n, v, f) sjaf_buffered_write_utf8_attrib(n, v, f)
#else
    #define SJAM_FLUSH_BUFFER(f)            /* Empty */
    #define SJAM_WRITE_BYTE(v, f)           sjaf_write_byte(v, f)
    #define SJAM_WRITE_JSHORT(v, f)         sjaf_write_jshort(v, f)
    #define SJAM_WRITE_JINT(v, f)           sjaf_write_jint(v, f)
    #define SJAM_WRITE_JLONG(v, f)          sjaf_write_jlong(v, f)
    #define SJAM_WRITE_BYTES(v, n, f)       sjaf_write_bytes(v, n, f)
    #define SJAM_WRITE_EVENT(v, f)          sjaf_write_event(v, f)
    #define SJAM_WRITE_FIELD_MASK(v, f)     sjaf_write_field_mask(v, f)
    #define SJAM_WRITE_THREAD_ID(v, f)      sjaf_write_thread_id(v, f)
    #define SJAM_WRITE_OBJECT_ID(v, f)      sjaf_write_object_id(v, f)
    #define SJAM_WRITE_CLASS_ID(v, f)       sjaf_write_object_id(v, f)
    #define SJAM_WRITE_METHOD_ID(v, f)      sjaf_write_method_id(v, f)
    #define SJAM_WRITE_ARENA_ID(v, f)       sjaf_write_arena_id(v, f)
    #define SJAM_WRITE_JNI_REF_ID(v, f)     sjaf_write_jni_ref_id(v, f)
    #define SJAM_WRITE_RAW_MONITOR_ID(v, f) sjaf_write_raw_monitor_id(v, f)
    #define SJAM_WRITE_UTF8(v, f)           sjaf_write_utf8(v, f)
    #define SJAM_WRITE_INST_OFFSET(v, f)    sjaf_write_inst_offset(v, f)
    #define SJAM_WRITE_JBOOLEAN(v, f)       sjaf_write_jboolean(v, f)
    #define SJAM_WRITE_JBOOLEAN_ATTRIB(n, v, f) sjaf_write_jboolean(n, v, f)
    #define SJAM_WRITE_JINT_ATTRIB(n, v, f) sjaf_write_jint_attrib(n, v, f)
    #define SJAM_WRITE_UTF8_ATTRIB(n, v, f) sjaf_write_utf8_attrib(n, v, f)
#endif

#endif
