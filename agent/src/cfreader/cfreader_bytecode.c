#include "cfreader_bytecode.h"

char cfrv_bytecode_lengths[256] = {
    1, // nop
    1, // aconst_null
    1, // iconst_m1
    1, // iconst_0
    1, // iconst_1
    1, // iconst_2
    1, // iconst_3
    1, // iconst_4
    1, // iconst_5
    1, // lconst_0
    1, // lconst_1
    1, // fconst_0
    1, // fconst_1
    1, // fconst_2
    1, // dconst_0
    1, // dconst_1
    2, // bipush
    3, // sipush
    2, // ldc
    3, // ldc_w
    3, // ldc2_w
    2, // iload
    2, // lload
    2, // fload
    2, // dload
    2, // aload
    1, // iload_0
    1, // iload_1
    1, // iload_2
    1, // iload_3
    1, // lload_0
    1, // lload_1
    1, // lload_2
    1, // lload_3
    1, // fload_0
    1, // fload_1
    1, // fload_2
    1, // fload_3
    1, // dload_0
    1, // dload_1
    1, // dload_2
    1, // dload_3
    1, // aload_0
    1, // aload_1
    1, // aload_2
    1, // aload_3
    1, // iaload
    1, // laload
    1, // faload
    1, // daload
    1, // aaload
    1, // baload
    1, // caload
    1, // saload
    2, // istore
    2, // lstore
    2, // fstore
    2, // dstore
    2, // astore
    1, // istore_0
    1, // istore_1
    1, // istore_2
    1, // istore_3
    1, // lstore_0
    1, // lstore_1
    1, // lstore_2
    1, // lstore_3
    1, // fstore_0
    1, // fstore_1
    1, // fstore_2
    1, // fstore_3
    1, // dstore_0
    1, // dstore_1
    1, // dstore_2
    1, // dstore_3
    1, // astore_0
    1, // astore_1
    1, // astore_2
    1, // astore_3
    1, // iastore
    1, // lastore
    1, // fastore
    1, // dastore
    1, // aastore
    1, // bastore
    1, // castore
    1, // sastore
    1, // pop
    1, // pop2
    1, // dup
    1, // dup_x1
    1, // dup_x2
    1, // dup2
    1, // dup2_x1
    1, // dup2_x2
    1, // swap
    1, // iadd
    1, // ladd
    1, // fadd
    1, // dadd
    1, // isub
    1, // lsub
    1, // fsub
    1, // dsub
    1, // imul
    1, // lmul
    1, // fmul
    1, // dmul
    1, // idiv
    1, // ldiv
    1, // fdiv
    1, // ddiv
    1, // irem
    1, // lrem
    1, // frem
    1, // drem
    1, // ineg
    1, // lneg
    1, // fneg
    1, // dneg
    1, // ishl
    1, // lshl
    1, // ishr
    1, // lshr
    1, // iushr
    1, // lushr
    1, // iand
    1, // land
    1, // ior
    1, // lor
    1, // ixor
    1, // lxor
    3, // iinc
    1, // i2l
    1, // i2f
    1, // i2d
    1, // l2i
    1, // l2f
    1, // l2d
    1, // f2i
    1, // f2l
    1, // f2d
    1, // d2i
    1, // d2l
    1, // d2f
    1, // int2byte
    1, // int2char
    1, // int2short
    1, // lcmp
    1, // fcmpl
    1, // fcmpg
    1, // dcmpl
    1, // dcmpg
    3, // ifeq
    3, // ifne
    3, // iflt
    3, // ifge
    3, // ifgt
    3, // ifle
    3, // if_icmpeq
    3, // if_icmpne
    3, // if_icmplt
    3, // if_icmpge
    3, // if_icmpgt
    3, // if_icmple
    3, // if_acmpeq
    3, // if_acmpne
    3, // goto
    3, // jsr
    2, // ret
   -1, // tableswitch
   -1, // lookupswitch
    1, // ireturn
    1, // lreturn
    1, // freturn
    1, // dreturn
    1, // areturn
    1, // return
    3, // getstatic
    3, // putstatic
    3, // getfield
    3, // putfield
    3, // invokevirtual
    3, // invokenonvirtual
    3, // invokestatic
    5, // invokeinterface
   -2, // xxxunusedxxx
    3, // new
    2, // newarray
    3, // anewarray
    1, // arraylength
    1, // athrow
    3, // checkcast
    3, // instanceof
    1, // monitorenter
    1, // monitorexit
   -1, // wide
    4, // multianewarray
    3, // ifnull
    3, // ifnonnull
    5, // goto_w
    5, // jsr_w
   -2, // 202
   -2, // 203
   -2, // 204
   -2, // 205
   -2, // 206
   -2, // 207
   -2, // 208
   -2, // 209
   -2, // 210
   -2, // 211
   -2, // 212
   -2, // 213
   -2, // 214
   -2, // 215
   -2, // 216
   -2, // 217
   -2, // 218
   -2, // 219
   -2, // 220
   -2, // 221
   -2, // 222
   -2, // 223
   -2, // 224
   -2, // 225
   -2, // 226
   -2, // 227
   -2, // 228
   -2, // 229
   -2, // 230
   -2, // 231
   -2, // 232
   -2, // 233
   -2, // 234
   -2, // 235
   -2, // 236
   -2, // 237
   -2, // 238
   -2, // 239
   -2, // 240
   -2, // 241
   -2, // 242
   -2, // 243
   -2, // 244
   -2, // 245
   -2, // 246
   -2, // 247
   -2, // 248
   -2, // 249
   -2, // 250
   -2, // 251
   -2, // 252
   -2, // 253
   -1, // impdep1  254
   -2  // 255
};

int cfrf_get_bytecode(cfrt_method_info *method, cfrt_bytecode *bytecode) {
    u2 i;

    if (method == NULL) {
        return CFREADER_ERR_NULL_PTR;
    }

    for (i = 0; i < method->attributes_count; i++) {
        cfrt_attribute_info *attribute;
        
        attribute = method->attributes + i;

        if (attribute->type == CFREADER_ATTR_TYPE_CODE) {
            u4 i;
            u4 length = attribute->u.Code.code_length;
            u1 *old_code = attribute->u.Code.code;
            u1 *new_code = CFRM_NEW_ARRAY(u1, length);

            if (new_code == NULL) {
                return CFREADER_ERR_MALLOC;
            }
            
            for (i = 0; i < length; i++) {
                new_code[i] = old_code[i];
            }
            
            bytecode->code = new_code;
            bytecode->length = length;
            return CFREADER_OK;
        }
    }

    return CFREADER_ERR_NOT_FOUND;
}

u4 cfrf_predict_next_offset(cfrt_bytecode *bytecode, u4 offset) {
    u1 bc_inst;
    u4 result = 0;

    /* TODO: Implement a real branch predictor */
    
    u1 *code = bytecode->code;
    bc_inst = code[offset];

    switch (bc_inst) {
        case JBC_GOTO:
        case JBC_IF_ICMPLT:
            /* Predict that this branch will be taken based on the code that is
             * generated by javac for a simple 'for' loop */
            result = (code[offset + 1] << 8) | code[offset + 2];
            break;
        case JBC_GOTO_W:
            result = (code[offset + 1] << 24) | (code[offset + 2] << 16)
                    | (code[offset + 3] << 8) | code[offset + 4];
            break;
        default:
            result = offset + cfrv_bytecode_lengths[bc_inst];
            break;
    }

    /* Check if the prediction falls between the accepted bounds */
    if (result >= bytecode->length) {
        /* Default, safe prediction, and inexpensive prediction:
         * predict the current instruction to be executed again.
         * (In most cases, we won't be able to do anything better since
         * we don't predict accross method boundaries.
         */
        result = offset;
    }

    return result;
}
