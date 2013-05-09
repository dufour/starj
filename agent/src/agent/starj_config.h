#ifndef _STARJ_CONFIG_H
#define _STARJ_CONFIG_H

/* ========================================================================== *
 *                             Compilation Flags                              *
 * ========================================================================== */

/* Enable support for UN*X FIFO files */
#define STARJ_ENABLE_PIPE

/* Enable support for 'tagging' events with the instruction count
 * (like it is done in EVolve) */
#define STARJ_ENABLE_BC_TAGGING

/* Synchronize access to the output file */
#define STARJ_LOCK_IO     

/* Synchronize access to the global vars */
#define STARJ_LOCK_GLOBAL_VARS 

/* Synchronize access to the class resolver */
#define STARJ_LOCK_CLASS_RESOLVER 

/* Provide hints to the compiler to inline some functions */
#define STARJ_USE_INLINE

/* Use coloured IO (can be turned off using options) */
#define STARJ_USE_COLOURS 

/* Size of a pointer (has to be 1,2,4 or 8) */
#define STARJ_PTR_SIZE 4

/* Encode instruction offsets as 4-byte values (wasteful in current class file
 * format) */
/* #define STARJ_LARGE_INST_OFFSETS */

/* Use buffered trace IO */
#define STARJ_BUFFERED_TRACE_IO


/* ========================================================================== * 
 *                             Debugging Options                              * 
 * ========================================================================== */

/* Trace received events */
/* #define STARJ_DBG_TRACE_EVENTS */

/* Trace event requests */
/* #define STARJ_DBG_TRACE_REQUESTS */

#endif
