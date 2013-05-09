#ifndef _STARJ_REQUESTS_H
#define _STARJ_REQUESTS_H

#include "starj_global.h"

SJAM_INLINE void sjaf_ensure_thread_id(sjat_thread_id env_id, sjat_event handler);
SJAM_INLINE void sjaf_ensure_object_id(sjat_object_id obj_id, sjat_event handler);
SJAM_INLINE void sjaf_ensure_class_id(sjat_class_id class_id, sjat_event handler);
SJAM_INLINE void sjaf_ensure_method_id(sjat_method_id method_id, sjat_event handler);

#endif
