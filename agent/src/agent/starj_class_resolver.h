#ifndef _STARJ_CLASS_RESOLVER_H
#define _STARJ_CLASS_RESOLVER_H

#include "starj_global.h"
#include "../cfreader/cfreader.h"

int sjaf_class_resolver_init();
int sjaf_class_resolver_release();
int sjaf_resolve_class(const char *class_name, cfrt_classfile *c);

#endif
