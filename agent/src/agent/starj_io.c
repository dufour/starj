#include "starj_io.h"
#include "starj_global.h"
#include "starj_id_resolver.h"

#include <stdarg.h>
#include <string.h>
#include <zlib.h>

static byte io_buffer[STARJ_IO_BUFFER_SIZE];
static int buff_count = 0;

void sjaf_debug(char *format, ...) {
    va_list ap;

    if (!(sjav_verbosity_level & STARJ_VERBOSE_DEBUG)) {
        return;
    }

#ifdef STARJ_USE_COLOURS
    if (sjav_colours) {
        fprintf(sjav_out_stream, "\x1b[32;06m");
    }
#endif
    fprintf(sjav_out_stream, "*J Debug> ");
#ifdef STARJ_USE_COLOURS
    if (sjav_colours) {
        fprintf(sjav_out_stream, "\x1b[0m");
    }
#endif
    
    va_start(ap, format);
    vfprintf(sjav_out_stream, format, ap);
    va_end(ap);

    fprintf(sjav_out_stream, "\n");
}

void sjaf_extra(char *format, ...) {
    va_list ap;

    if (!(sjav_verbosity_level & STARJ_VERBOSE_EXTRA)) {
        return;
    }

#ifdef STARJ_USE_COLOURS
    if (sjav_colours) {
        fprintf(sjav_out_stream, "\x1b[34;06m");
    }
#endif
    fprintf(sjav_out_stream, "*J Agent> ");
#ifdef STARJ_USE_COLOURS
    if (sjav_colours) {
        fprintf(sjav_out_stream, "\x1b[0m");
    }
#endif
    
    va_start(ap, format);
    vfprintf(sjav_out_stream, format, ap);
    va_end(ap);

    fprintf(sjav_out_stream, "\n");
}

void sjaf_message(char *format, ...) {
    va_list ap;

    if (!(sjav_verbosity_level & STARJ_VERBOSE_MESS)) {
        return;
    }

    fprintf(sjav_out_stream, "*J Agent> ");
    
    va_start(ap, format);
    vfprintf(sjav_out_stream, format, ap);
    va_end(ap);

    fprintf(sjav_out_stream, "\n");
}

void sjaf_warning(char *format, ...) {
    va_list ap;

    if (!(sjav_verbosity_level & STARJ_VERBOSE_WARN)) {
        return;
    }

#ifdef STARJ_USE_COLOURS
    if (sjav_colours) {
        fprintf(sjav_out_stream, "\x1b[34;01m");
    }
#endif
    fprintf(sjav_out_stream, "*J Warning> ");
#ifdef STARJ_USE_COLOURS
    if (sjav_colours) {
        fprintf(sjav_out_stream, "\x1b[0m");
    }
#endif
    
    va_start(ap, format);
    vfprintf(sjav_out_stream, format, ap);
    va_end(ap);

    fprintf(sjav_out_stream, "\n");
}

void sjaf_error(char *format, ...) {
    va_list ap;

    if (!(sjav_verbosity_level & STARJ_VERBOSE_ERR)) {
        return;
    }

#ifdef STARJ_USE_COLOURS
    if (sjav_colours) {
        fprintf(sjav_out_stream, "\x1b[31;06m");
    }
#endif
    fprintf(sjav_out_stream, "*J Error> ");
#ifdef STARJ_USE_COLOURS
    if (sjav_colours) {
        fprintf(sjav_out_stream, "\x1b[0m");
    }
#endif
    
    va_start(ap, format);
    vfprintf(sjav_out_stream, format, ap);
    va_end(ap);

    fprintf(sjav_out_stream, "\n");
}

/* ========================================================================== *
 *                              Input Functions                               *
 * ========================================================================== */

/* Generic macros */

#define GENERIC_READ_U1(var, file) \
    if (fread(var, 1, 1, file) == 1) {\
        return STARJ_OK;\
    }\
\
    return STARJ_ERR_IO;

#define GENERIC_READ(type, var, file) \
    int _j;\
    size_t _type_size = sizeof(type);\
    type _result = (type) 0;\
\
    for (_j = _type_size - 1; _j >= 0; _j--) {\
        byte _c;\
\
        if (fread(&_c, 1, 1, file) != 1) {\
            return STARJ_ERR_IO;\
        }\
\
        _result |= ((type) _c) << (_j * 8);\
    }\
\
    *((type *)var) = _result;\
    return STARJ_OK;

#define GENERIC_READ_BYTES(var, count_type, count, file) \
    if (((count_type)fread(var, 1, count, file)) == count) {\
        return STARJ_OK;\
    }\
\
    return STARJ_ERR_IO;

#define GENERIC_READ_U2(var, file) GENERIC_READ(jshort, var, file)
#define GENERIC_READ_U4(var, file) GENERIC_READ(jint, var, file)
#define GENERIC_READ_U8(var, file) GENERIC_READ(jlong, var, file)
#if (STARJ_PTR_SIZE == 1)
    #define GENERIC_READ_PTR(var, file) GENERIC_READ_U1(var, file)
#elif (STARJ_PTR_SIZE == 2)
    #define GENERIC_READ_PTR(var, file) GENERIC_READ_U2(var, file)
#elif (STARJ_PTR_SIZE == 4)
    #define GENERIC_READ_PTR(var, file) GENERIC_READ_U4(var, file)
#else
    #define GENERIC_READ_PTR(var, file) GENERIC_READ_U8(var, file)
#endif

int sjaf_read_byte(byte *b, void *f) {
    GENERIC_READ_U1(b, f)
}

int sjaf_read_jshort(jshort *s, void *f) {
    GENERIC_READ_U2(s, f)
}

int sjaf_read_jint(jint *i, void *f) {
    GENERIC_READ_U4(i, f)
}

int sjaf_read_jlong(jlong *l, void *f) {
    GENERIC_READ_U8(l, f)
}

int sjaf_read_bytes(byte *s, jint count, void *f) {
    GENERIC_READ_BYTES(s, jint, count, f)
}

int sjaf_read_event(sjat_event *event, void *f) {
    GENERIC_READ_U1(event, f)
}

int sjaf_read_field_mask(sjat_field_mask *m, void *f) {
    GENERIC_READ_U4(m, f)
}

int sjaf_read_thread_id(sjat_thread_id *t, void *f) {
    GENERIC_READ_PTR(t, f)
}

int sjaf_read_object_id(sjat_object_id *obj, void *f) {
    GENERIC_READ_PTR(obj, f)
}

int sjaf_read_class_id(sjat_class_id *c, void *f) {
    GENERIC_READ_PTR(c, f)
}

int sjaf_read_method_id(sjat_method_id *m, void *f) {
    GENERIC_READ_PTR(m, f)
}

int sjaf_read_arena_id(sjat_arena_id *a, void *f) {
    GENERIC_READ_PTR(a, f)
}

int sjaf_read_jni_ref_id(sjat_jni_ref_id *j, void *f) {
    GENERIC_READ_PTR(j, f)
}

int sjaf_read_raw_monitor_id(sjat_raw_monitor_id *m, void *f) {
    GENERIC_READ_PTR(m, f)
}

int sjaf_read_jboolean(jboolean *b, void *f) {
    GENERIC_READ_U1(b, f);
}

/* Output functions (unbuffered) */

#define GENERIC_WRITE_U1(var, file) \
    if (sjav_gzip_output) {\
        if (gzwrite(file, &var, 1) != 1) {\
            return STARJ_ERR_IO;\
        }\
    } else {\
        if (fwrite(&var, 1, 1, file) != 1) {\
            return STARJ_ERR_IO;\
        }\
    }\
    sjav_current_file_size += 1;\
\
    return STARJ_OK;

#define GENERIC_WRITE(type, var, file) \
    int _i, _j;\
    size_t _type_size = sizeof(type);\
    byte buff[sizeof(type)];\
\
    for (_i = 0, _j = _type_size - 1; _j >= 0; _i++, _j--) {\
        buff[_i] = (byte) ((((type) var) >> (_j * 8)) & 0x000000FF);\
    }\
\
    if (sjav_gzip_output) {\
        if (gzwrite(file, buff, _type_size) != _type_size) {\
            return STARJ_ERR_IO;\
        }\
    } else {\
        if (fwrite(buff, 1, _type_size, file) != _type_size) {\
            return STARJ_ERR_IO;\
        }\
    }\
    sjav_current_file_size += _type_size;\
\
    return STARJ_OK;

#define GENERIC_WRITE_BYTES(var, count_type, count, file) \
    if (sjav_gzip_output) {\
        if (((count_type)gzwrite(file, (void *)var, count)) == count) {\
            return STARJ_OK;\
        }\
    } else {\
        if (((count_type)fwrite(var, 1, count, file)) == count) {\
            return STARJ_OK;\
        }\
    }\
    sjav_current_file_size += count;\
\
    return STARJ_ERR_IO;

#define GENERIC_WRITE_U2(var, file) GENERIC_WRITE(jshort, var, file)
#define GENERIC_WRITE_U4(var, file) GENERIC_WRITE(jint, var, file)
#define GENERIC_WRITE_U8(var, file) GENERIC_WRITE(jlong, var, file)
#if (STARJ_PTR_SIZE == 1)
    #define GENERIC_WRITE_PTR(var, file) GENERIC_WRITE_U1(var, file)
#elif (STARJ_PTR_SIZE == 2)
    #define GENERIC_WRITE_PTR(var, file) GENERIC_WRITE_U2(var, file)
#elif (STARJ_PTR_SIZE == 4)
    #define GENERIC_WRITE_PTR(var, file) GENERIC_WRITE_U4(var, file)
#else
    #define GENERIC_WRITE_PTR(var, file) GENERIC_WRITE_U8(var, file)
#endif

int sjaf_write_byte(byte b, void *f) {
    GENERIC_WRITE_U1(b, f)
}

int sjaf_write_jshort(jshort s, void *f) {
    GENERIC_WRITE_U2(s, f)
}

int sjaf_write_jint(jint i, void *f) {
    GENERIC_WRITE_U4(i, f)
}

int sjaf_write_jlong(jlong l, void *f) {
    GENERIC_WRITE_U8(l, f)
}

int sjaf_write_bytes(const byte *s, jint count, void *f) {
    GENERIC_WRITE_BYTES(s, jint, count, f)
}

int sjaf_write_event(sjat_event event, void *f) {
    GENERIC_WRITE_U1(event, f)
}

int sjaf_write_field_mask(sjat_field_mask m, void *f) {
    GENERIC_WRITE_U4(m, f)
}

int sjaf_write_thread_id(sjat_thread_id t, void *f) {
    if (sjav_unique_ids) {
        jint unique_id = sjaf_resolve_thread_id(t);
        GENERIC_WRITE_U4(unique_id, f)
    } else {
        GENERIC_WRITE_PTR(t, f)
    }
}

int sjaf_write_object_id(sjat_object_id obj, void *f) {
    if (sjav_unique_ids) {
        jlong unique_id = sjaf_resolve_object_id(obj);
        GENERIC_WRITE_U8(unique_id, f)
    } else {
        GENERIC_WRITE_PTR(obj, f)
    }
}

int sjaf_write_class_id(sjat_class_id c, void *f) {
    if (sjav_unique_ids) {
        jlong unique_id = sjaf_resolve_object_id((sjat_object_id) c);
        GENERIC_WRITE_U8(unique_id, f)
    } else {
        GENERIC_WRITE_PTR(c, f)
    }
}

int sjaf_write_method_id(sjat_method_id m, void *f) {
    if (sjav_unique_ids) {
        jint unique_id = sjaf_resolve_method_id(m);
        GENERIC_WRITE_U4(unique_id, f)
    } else {
        GENERIC_WRITE_PTR(m, f)
    }
}

int sjaf_write_arena_id(sjat_arena_id a, void *f) {
    if (sjav_unique_ids) {
        jlong unique_id = sjaf_resolve_arena_id(a);
        GENERIC_WRITE_U8(unique_id, f)
    } else {
        GENERIC_WRITE_PTR(a, f)
    }
}

int sjaf_write_jni_ref_id(sjat_jni_ref_id j, void *f) {
    GENERIC_WRITE_PTR(j, f)
}

int sjaf_write_raw_monitor_id(sjat_raw_monitor_id m, void *f) {
    if (sjav_unique_ids) {
        jint unique_id = sjaf_resolve_raw_monitor_id(m);
        GENERIC_WRITE_U4(unique_id, f)
    } else {
        GENERIC_WRITE_PTR(m, f)
    }
}

int sjaf_write_utf8(const char *s, void *f) {
    jshort len;
    int result;
    
    if (s == NULL) {
        s = SJAM_NULL_STR;
    }
    
    len = (jshort) (strlen(s) & 0x0000FFFF);
    result = sjaf_write_jshort(len, f);
    if (result != STARJ_OK) {
        return result;
    }
    return sjaf_write_bytes(s, len, f);
}

int sjaf_write_inst_offset(jint offset, void *f) {
#ifdef STARJ_LARGE_INST_OFFSETS
    GENERIC_WRITE_U4(offset, f);
#else
    jshort value = ((jshort) (offset & 0x0000FFFF));
    GENERIC_WRITE_U2(value, f)
#endif
}

int sjaf_write_jboolean(jboolean b, void *f) {
    GENERIC_WRITE_U1(b, f);
}

int write_attrib_header(const char *name, jint size, void *f) {
    int result;

    result = sjaf_write_utf8(name, f);
    if (result != STARJ_OK) {
        return result;
    }

    return sjaf_write_jint(size, f);
}

int sjaf_write_jboolean_attrib(const char *name, jboolean value, void *f) {
    int result;

    result = write_attrib_header(name, 1, f);
    if (result != STARJ_OK) {
        return result;
    }

    return sjaf_write_jboolean(value, f);
}

int sjaf_write_jint_attrib(const char *name, jint value, void *f) {
    int result;

    result = write_attrib_header(name, sizeof(jint), f);
    if (result != STARJ_OK) {
        return result;
    }

    return sjaf_write_jint(value, f);
}

int sjaf_write_utf8_attrib(const char *name, const char *value,
        void *f) {
    int result;

    if (value == NULL) {
        value = SJAM_NULL_STR;
    }

    result = write_attrib_header(name, sizeof(jshort) + strlen(value),
            f);
    if (result != STARJ_OK) {
        return result;
    }

    return sjaf_write_utf8(value, f);
}

/* Output functions (buffered) */

int sjaf_flush_buffer(void *f) {
    int num_written;

    if (buff_count <= 0) {
        return STARJ_OK;
    }
    
    if (sjav_gzip_output) {
        num_written = gzwrite(f, io_buffer, buff_count);
    } else {
        num_written = fwrite(io_buffer, 1, buff_count, f);
    }
    if (num_written != buff_count) {
        int i;

        for (i = num_written; i < buff_count; i++) {
            io_buffer[i - num_written] = io_buffer[i];
        }

        buff_count -= num_written;
        
        return STARJ_ERR_IO;
    }
    buff_count = 0;
    
    return STARJ_OK;
}

#define GENERIC_BUFFERED_WRITE_U1(var, file) \
    if ((buff_count + sizeof(byte)) > STARJ_IO_BUFFER_SIZE) {\
        if (sjaf_flush_buffer(file) != STARJ_OK) {\
            return STARJ_ERR_IO;\
        }\
    }\
\
    io_buffer[buff_count++] = var;\
    sjav_current_file_size += 1;\
\
    return STARJ_OK;

#define GENERIC_BUFFERED_WRITE(type, var, file) \
    int _i;\
\
    if ((buff_count + sizeof(type)) > STARJ_IO_BUFFER_SIZE) {\
        if (sjaf_flush_buffer(file) != STARJ_OK) {\
            return STARJ_ERR_IO;\
        }\
    }\
\
    for (_i = sizeof(type) - 1; _i >= 0; _i--) {\
        io_buffer[buff_count++] = (byte) ((((type) var) >> (_i * 8)) & 0x000000FF);\
    }\
    sjav_current_file_size += sizeof(type);\
\
    return STARJ_OK;

#define GENERIC_BUFFERED_WRITE_BYTES(var, count_type, count, file) \
    count_type _i;\
\
    if ((buff_count + count) > STARJ_IO_BUFFER_SIZE) {\
        if (sjaf_flush_buffer(file) != STARJ_OK) {\
            return STARJ_ERR_IO;\
        }\
    }\
    if (count > STARJ_IO_BUFFER_SIZE) {\
        return sjaf_write_bytes(var, count, file);\
    }\
\
    for (_i = 0; _i < count; _i++) {\
        io_buffer[buff_count++] = (byte) var[_i];\
    }\
    sjav_current_file_size += count;\
\
    return STARJ_OK;

#define GENERIC_BUFFERED_WRITE_U2(var, file) GENERIC_BUFFERED_WRITE(jshort, var, file)
#define GENERIC_BUFFERED_WRITE_U4(var, file) GENERIC_BUFFERED_WRITE(jint, var, file)
#define GENERIC_BUFFERED_WRITE_U8(var, file) GENERIC_BUFFERED_WRITE(jlong, var, file)
#if (STARJ_PTR_SIZE == 1)
    #define GENERIC_BUFFERED_WRITE_PTR(var, file) GENERIC_BUFFERED_WRITE_U1(var, file)
#elif (STARJ_PTR_SIZE == 2)
    #define GENERIC_BUFFERED_WRITE_PTR(var, file) GENERIC_BUFFERED_WRITE_U2(var, file)
#elif (STARJ_PTR_SIZE == 4)
    #define GENERIC_BUFFERED_WRITE_PTR(var, file) GENERIC_BUFFERED_WRITE_U4(var, file)
#else
    #define GENERIC_BUFFERED_WRITE_PTR(var, file) GENERIC_BUFFERED_WRITE_U8(var, file)
#endif

int sjaf_buffered_write_byte(byte b, void *f) {
    GENERIC_BUFFERED_WRITE_U1(b, f)
}

int sjaf_buffered_write_jboolean(jboolean b, void *f) {
    GENERIC_BUFFERED_WRITE_U1(b, f)
}

int sjaf_buffered_write_jshort(jshort s, void *f) {
    GENERIC_BUFFERED_WRITE_U2(s, f)
}

int sjaf_buffered_write_jint(jint i, void *f) {
    GENERIC_BUFFERED_WRITE_U4(i, f)
}

int sjaf_buffered_write_jlong(jlong l, void *f) {
    GENERIC_BUFFERED_WRITE_U8(l, f)
}

int sjaf_buffered_write_bytes(const byte *s, jint count, void *f) {
    GENERIC_BUFFERED_WRITE_BYTES(s, jint, count, f)
}

int sjaf_buffered_write_event(sjat_event event, void *f) {
    GENERIC_BUFFERED_WRITE_U1(event, f)
}

int sjaf_buffered_write_field_mask(sjat_field_mask m, void *f) {
    GENERIC_BUFFERED_WRITE_U4(m, f)
}

int sjaf_buffered_write_thread_id(sjat_thread_id t, void *f) {
    if (sjav_unique_ids) {
        jint unique_id = sjaf_resolve_thread_id(t);
        GENERIC_BUFFERED_WRITE_U4(unique_id, f)
    } else {
        GENERIC_BUFFERED_WRITE_PTR(t, f)
    }
}

int sjaf_buffered_write_object_id(sjat_object_id obj, void *f) {
    if (sjav_unique_ids) {
        jlong unique_id = sjaf_resolve_object_id(obj);
        GENERIC_BUFFERED_WRITE_U8(unique_id, f)
        return 0;
    } else {
        GENERIC_BUFFERED_WRITE_PTR(obj, f)
    }
}

int sjaf_buffered_write_class_id(sjat_class_id c, void *f) {
    if (sjav_unique_ids) {
        jlong unique_id = sjaf_resolve_object_id((sjat_object_id) c);
        GENERIC_BUFFERED_WRITE_U8(unique_id, f)
    } else {
        GENERIC_BUFFERED_WRITE_PTR(c, f)
    }
}

int sjaf_buffered_write_method_id(sjat_method_id m, void *f) {
    if (sjav_unique_ids) {
        jint unique_id = sjaf_resolve_method_id(m);
        GENERIC_BUFFERED_WRITE_U4(unique_id, f)
    } else {
        GENERIC_BUFFERED_WRITE_PTR(m, f)
    }
}

int sjaf_buffered_write_arena_id(sjat_arena_id a, void *f) {
    if (sjav_unique_ids) {
        jlong unique_id = sjaf_resolve_arena_id(a);
        GENERIC_BUFFERED_WRITE_U8(unique_id, f)
    } else {
        GENERIC_BUFFERED_WRITE_PTR(a, f)
    }
}

int sjaf_buffered_write_jni_ref_id(sjat_jni_ref_id j, void *f) {
    GENERIC_BUFFERED_WRITE_PTR(j, f)
}

int sjaf_buffered_write_raw_monitor_id(sjat_raw_monitor_id m, void *f) {
    if (sjav_unique_ids) {
        jint unique_id = sjaf_resolve_raw_monitor_id(m);
        GENERIC_BUFFERED_WRITE_U4(unique_id, f)
    } else {
        GENERIC_BUFFERED_WRITE_PTR(m, f)
    }
}

int sjaf_buffered_write_utf8(const char *s, void *f) {
    jshort len;
    int result;

    if (s == NULL) {
        s = SJAM_NULL_STR;
    }
    
    len = (jshort) (strlen(s) & 0x0000FFFF);
    result = sjaf_buffered_write_jshort(len, f);
    if (result != STARJ_OK) {
        return result;
    }
    result = sjaf_buffered_write_bytes(s, len, f);
    
    return result;
}

int sjaf_buffered_write_inst_offset(jint offset, void *f) {
#ifdef STARJ_LARGE_INST_OFFSETS
    GENERIC_BUFFERED_WRITE_U4(offset, f)
#else
    jshort value = ((jshort) (offset & 0x0000FFFF));
    GENERIC_BUFFERED_WRITE_U2(value, f)
#endif
}

int buffered_write_attrib_header(const char *name, jint size, void *f) {
    int result;

    result = sjaf_buffered_write_utf8(name, f);
    if (result != STARJ_OK) {
        return result;
    }

    return sjaf_buffered_write_jint(size, f);
}

int sjaf_buffered_write_jboolean_attrib(const char *name, jboolean value,
        void *f) {
    int result;

    result = buffered_write_attrib_header(name, 1, f);
    if (result != STARJ_OK) {
        return result;
    }

    return sjaf_buffered_write_jboolean(value, f);
}

int sjaf_buffered_write_jint_attrib(const char *name, jint value, void *f) {
    int result;

    result = buffered_write_attrib_header(name, sizeof(jint), f);
    if (result != STARJ_OK) {
        return result;
    }

    return sjaf_buffered_write_jint(value, f);
}

int sjaf_buffered_write_utf8_attrib(const char *name, const char *value,
        void *f) {
    int result;

    if (value == NULL) {
        value = SJAM_NULL_STR;
    }

    result = buffered_write_attrib_header(name, sizeof(jshort) + strlen(value),
            f);
    if (result != STARJ_OK) {
        return result;
    }

    return sjaf_buffered_write_utf8(value, f);
}
