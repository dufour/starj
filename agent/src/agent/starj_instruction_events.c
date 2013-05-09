#include "starj_instruction_events.h"
#include "starj_io.h"
#include "starj_spec.h"
#include "starj_requests.h"
#include "../cfreader/cfreader_bytecode.h"

struct {
    sjat_thread_id env_id;
    sjat_method_id method_id;
    jint start;
    jint last_offset;
    jint count;
} pending_ise;

static bool ise_is_pending = false;

void sjaf_clear_pending_ise() {
    SJAM_START_GLOBAL_ACCESS();
    if (ise_is_pending) {
        if (pending_ise.count > 1) {
            SJAM_START_IO();
            SJAM_WRITE_EVENT(STARJ_EVENT_COMPACT_INSTRUCTION_START, sjav_file);
            SJAM_BC_TAG(sjav_file);
            SJAM_WRITE_THREAD_ID(pending_ise.env_id, sjav_file);
            SJAM_WRITE_METHOD_ID(pending_ise.method_id, sjav_file);
            SJAM_WRITE_INST_OFFSET(pending_ise.start, sjav_file);
            SJAM_WRITE_INST_OFFSET(pending_ise.count, sjav_file);
            SJAM_END_IO();
        } else {
            SJAM_START_IO();
            SJAM_WRITE_EVENT(STARJ_EVENT_INSTRUCTION_START, sjav_file);
            SJAM_BC_TAG(sjav_file);
            SJAM_WRITE_THREAD_ID(pending_ise.env_id, sjav_file);
            SJAM_WRITE_METHOD_ID(pending_ise.method_id, sjav_file);
            SJAM_WRITE_INST_OFFSET(pending_ise.start, sjav_file);
            SJAM_END_IO();
        }

        ise_is_pending = false;
    }
    SJAM_END_GLOBAL_ACCESS();
}

void sjaf_handler_instruction_start(sjat_thread_id env_id, sjat_method_id method_id,
        jint offset, jboolean is_true, jint key, jint low, jint hi,
        jint chosen_pair_index, jint pairs_total, bool requested) {
    sjat_event event_id = STARJ_EVENT_INSTRUCTION_START;
    sjat_field_mask m = sjav_event_masks[event_id];

    SJAM_START_GLOBAL_ACCESS();
    sjav_total_executed_insts++;
    SJAM_END_GLOBAL_ACCESS();

    /* sjaf_ensure_thread_id(env_id, event_id); */
    sjaf_ensure_method_id(method_id, event_id);

    if (sjav_optimize_mode) {
        /* Note: starj_setup is responsible for disabling the optimization
         * mode when not applicable. */
        SJAM_START_GLOBAL_ACCESS();
        if (ise_is_pending) {
            /* Check if we can just add this instruction to
             * the pending Instruction Start Event (ISE) */
            if ((pending_ise.env_id == env_id) &&
                (pending_ise.method_id == method_id)) {
                cfrt_bytecode *bytecode;
                int result;
                
                /* Maybe */
                result = sjdf_hash_map_get(&sjav_method_id_to_bytecode, method_id, (sjdt_key *) &bytecode);
                if (result == STARJ_DATA_OK) {
                    u4 prediction = cfrf_predict_next_offset(bytecode, (u4) pending_ise.last_offset);
                    if (offset == prediction) {
                        /* OK */
                        pending_ise.count += 1;
                        pending_ise.last_offset = offset;
                        SJAM_END_GLOBAL_ACCESS();
                        return;
                    }
                } else {
                    //sjaf_warning("Failed to get bytecode for method 0x%p (result = %d)", method_id, result);
                }
            }
            
            /* We can't */
            sjaf_clear_pending_ise();
        } else {
            pending_ise.env_id = env_id;
            pending_ise.method_id = method_id;
            pending_ise.start = offset;
            pending_ise.last_offset = offset;
            pending_ise.count = 1;

            ise_is_pending = true;

            SJAM_END_GLOBAL_ACCESS();
            return;
        }
        SJAM_END_GLOBAL_ACCESS();
    }
    
    if (requested) {
        event_id |= STARJ_REQUESTED_EVENT_MASK;
    }

    SJAM_START_IO();
    SJAM_WRITE_EVENT(event_id, sjav_file);
    SJAM_BC_TAG(sjav_file);
    if (m & STARJ_FIELD_ENV_ID) {
        SJAM_WRITE_THREAD_ID(env_id, sjav_file);
    }
    if (m & STARJ_FIELD_METHOD_ID) {
        SJAM_WRITE_METHOD_ID(method_id, sjav_file);
    }
    if (m & STARJ_FIELD_OFFSET) {
        SJAM_WRITE_INST_OFFSET(offset, sjav_file);
    }
    if (m & STARJ_FIELD_IS_TRUE) {
        SJAM_WRITE_JBOOLEAN(is_true, sjav_file);
    }
    if (m & STARJ_FIELD_KEY) {
        SJAM_WRITE_JINT(key, sjav_file);
    }
    if (m & STARJ_FIELD_LOW) {
        SJAM_WRITE_JINT(low, sjav_file);
    }
    if (m & STARJ_FIELD_HI) {
        SJAM_WRITE_JINT(hi, sjav_file);
    }
    if (m & STARJ_FIELD_CHOSEN_PAIR_INDEX) {
        SJAM_WRITE_JINT(chosen_pair_index, sjav_file);
    }
    if (m & STARJ_FIELD_PAIRS_TOTAL) {
        SJAM_WRITE_JINT(pairs_total, sjav_file);
    }
    SJAM_END_IO();
}
