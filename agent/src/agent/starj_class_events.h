#ifndef _STARJ_CLASS_EVENTS_H
#define _STARJ_CLASS_EVENTS_H

#include "starj_global.h"

void sjaf_handler_class_load(sjat_thread_id env_id, const char *name, const char *src_name,
        jint num_interfaces, jint num_methods, JVMPI_Method *methods,
        jint num_static_fields, JVMPI_Field *statics,
        jint num_instance_fields, JVMPI_Field *instances,
        sjat_class_id class_id, bool requested);
void sjaf_handler_class_unload(sjat_thread_id env_id, sjat_class_id class_id, bool requested);
void sjaf_handler_class_load_hook(const byte *class_data, jint class_data_len,
        byte **new_class_data, jint *new_class_data_len,
        void * (*malloc_f)(unsigned int), bool requested);

#endif
