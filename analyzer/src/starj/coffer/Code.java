package starj.coffer;

import java.io.*;

public class Code {
    public static final short UNKNOWN         = ((short)  -1);
    public static final short NOP             = ((short)  0);
    public static final short ACONST_NULL     = ((short)  1);
    public static final short ICONST_M1       = ((short)  2);
    public static final short ICONST_0        = ((short)  3);
    public static final short ICONST_1        = ((short)  4);
    public static final short ICONST_2        = ((short)  5);
    public static final short ICONST_3        = ((short)  6);
    public static final short ICONST_4        = ((short)  7);
    public static final short ICONST_5        = ((short)  8);
    public static final short LCONST_0        = ((short)  9);
    public static final short LCONST_1        = ((short) 10);
    public static final short FCONST_0        = ((short) 11);
    public static final short FCONST_1        = ((short) 12);
    public static final short FCONST_2        = ((short) 13);
    public static final short DCONST_0        = ((short) 14);
    public static final short DCONST_1        = ((short) 15);
    public static final short BIPUSH          = ((short) 16);
    public static final short SIPUSH          = ((short) 17);
    public static final short LDC             = ((short) 18);
    public static final short LDC_W           = ((short) 19);
    public static final short LDC2_W          = ((short) 20);
    public static final short ILOAD           = ((short) 21);
    public static final short LLOAD           = ((short) 22);
    public static final short FLOAD           = ((short) 23);
    public static final short DLOAD           = ((short) 24);
    public static final short ALOAD           = ((short) 25);
    public static final short ILOAD_0         = ((short) 26);
    public static final short ILOAD_1         = ((short) 27);
    public static final short ILOAD_2         = ((short) 28);
    public static final short ILOAD_3         = ((short) 29);
    public static final short LLOAD_0         = ((short) 30);
    public static final short LLOAD_1         = ((short) 31);
    public static final short LLOAD_2         = ((short) 32);
    public static final short LLOAD_3         = ((short) 33);
    public static final short FLOAD_0         = ((short) 34);
    public static final short FLOAD_1         = ((short) 35);
    public static final short FLOAD_2         = ((short) 36);
    public static final short FLOAD_3         = ((short) 37);
    public static final short DLOAD_0         = ((short) 38);
    public static final short DLOAD_1         = ((short) 39);
    public static final short DLOAD_2         = ((short) 40);
    public static final short DLOAD_3         = ((short) 41);
    public static final short ALOAD_0         = ((short) 42);
    public static final short ALOAD_1         = ((short) 43);
    public static final short ALOAD_2         = ((short) 44);
    public static final short ALOAD_3         = ((short) 45);
    public static final short IALOAD          = ((short) 46);
    public static final short LALOAD          = ((short) 47);
    public static final short FALOAD          = ((short) 48);
    public static final short DALOAD          = ((short) 49);
    public static final short AALOAD          = ((short) 50);
    public static final short BALOAD          = ((short) 51);
    public static final short CALOAD          = ((short) 52);
    public static final short SALOAD          = ((short) 53);
    public static final short ISTORE          = ((short) 54);
    public static final short LSTORE          = ((short) 55);
    public static final short FSTORE          = ((short) 56);
    public static final short DSTORE          = ((short) 57);
    public static final short ASTORE          = ((short) 58);
    public static final short ISTORE_0        = ((short) 59);
    public static final short ISTORE_1        = ((short) 60);
    public static final short ISTORE_2        = ((short) 61);
    public static final short ISTORE_3        = ((short) 62);
    public static final short LSTORE_0        = ((short) 63);
    public static final short LSTORE_1        = ((short) 64);
    public static final short LSTORE_2        = ((short) 65);
    public static final short LSTORE_3        = ((short) 66);
    public static final short FSTORE_0        = ((short) 67);
    public static final short FSTORE_1        = ((short) 68);
    public static final short FSTORE_2        = ((short) 69);
    public static final short FSTORE_3        = ((short) 70);
    public static final short DSTORE_0        = ((short) 71);
    public static final short DSTORE_1        = ((short) 72);
    public static final short DSTORE_2        = ((short) 73);
    public static final short DSTORE_3        = ((short) 74);
    public static final short ASTORE_0        = ((short) 75);
    public static final short ASTORE_1        = ((short) 76);
    public static final short ASTORE_2        = ((short) 77);
    public static final short ASTORE_3        = ((short) 78);
    public static final short IASTORE         = ((short) 79);
    public static final short LASTORE         = ((short) 80);
    public static final short FASTORE         = ((short) 81);
    public static final short DASTORE         = ((short) 82);
    public static final short AASTORE         = ((short) 83);
    public static final short BASTORE         = ((short) 84);
    public static final short CASTORE         = ((short) 85);
    public static final short SASTORE         = ((short) 86);
    public static final short POP             = ((short) 87);
    public static final short POP2            = ((short) 88);
    public static final short DUP             = ((short) 89);
    public static final short DUP_X1          = ((short) 90);
    public static final short DUP_X2          = ((short) 91);
    public static final short DUP2            = ((short) 92);
    public static final short DUP2_X1         = ((short) 93);
    public static final short DUP2_X2         = ((short) 94);
    public static final short SWAP            = ((short) 95);
    public static final short IADD            = ((short) 96);
    public static final short LADD            = ((short) 97);
    public static final short FADD            = ((short) 98);
    public static final short DADD            = ((short) 99);
    public static final short ISUB            = ((short)100);
    public static final short LSUB            = ((short)101);
    public static final short FSUB            = ((short)102);
    public static final short DSUB            = ((short)103);
    public static final short IMUL            = ((short)104);
    public static final short LMUL            = ((short)105);
    public static final short FMUL            = ((short)106);
    public static final short DMUL            = ((short)107);
    public static final short IDIV            = ((short)108);
    public static final short LDIV            = ((short)109);
    public static final short FDIV            = ((short)110);
    public static final short DDIV            = ((short)111);
    public static final short IREM            = ((short)112);
    public static final short LREM            = ((short)113);
    public static final short FREM            = ((short)114);
    public static final short DREM            = ((short)115);
    public static final short INEG            = ((short)116);
    public static final short LNEG            = ((short)117);
    public static final short FNEG            = ((short)118);
    public static final short DNEG            = ((short)119);
    public static final short ISHL            = ((short)120);
    public static final short LSHL            = ((short)121);
    public static final short ISHR            = ((short)122);
    public static final short LSHR            = ((short)123);
    public static final short IUSHR           = ((short)124);
    public static final short LUSHR           = ((short)125);
    public static final short IAND            = ((short)126);
    public static final short LAND            = ((short)127);
    public static final short IOR             = ((short)128);
    public static final short LOR             = ((short)129);
    public static final short IXOR            = ((short)130);
    public static final short LXOR            = ((short)131);
    public static final short IINC            = ((short)132);
    public static final short I2L             = ((short)133);
    public static final short I2F             = ((short)134);
    public static final short I2D             = ((short)135);
    public static final short L2I             = ((short)136);
    public static final short L2F             = ((short)137);
    public static final short L2D             = ((short)138);
    public static final short F2I             = ((short)139);
    public static final short F2L             = ((short)140);
    public static final short F2D             = ((short)141);
    public static final short D2I             = ((short)142);
    public static final short D2L             = ((short)143);
    public static final short D2F             = ((short)144);
    public static final short I2B             = ((short)145);
    public static final short I2C             = ((short)146);
    public static final short I2S             = ((short)147);
    public static final short LCMP            = ((short)148);
    public static final short FCMPL           = ((short)149);
    public static final short FCMPG           = ((short)150);
    public static final short DCMPL           = ((short)151);
    public static final short DCMPG           = ((short)152);
    public static final short IFEQ            = ((short)153);
    public static final short IFNE            = ((short)154);
    public static final short IFLT            = ((short)155);
    public static final short IFGE            = ((short)156);
    public static final short IFGT            = ((short)157);
    public static final short IFLE            = ((short)158);
    public static final short IF_ICMPEQ       = ((short)159);
    public static final short IF_ICMPNE       = ((short)160);
    public static final short IF_ICMPLT       = ((short)161);
    public static final short IF_ICMPGE       = ((short)162);
    public static final short IF_ICMPGT       = ((short)163);
    public static final short IF_ICMPLE       = ((short)164);
    public static final short IF_ACMPEQ       = ((short)165);
    public static final short IF_ACMPNE       = ((short)166);
    public static final short GOTO            = ((short)167);
    public static final short JSR             = ((short)168);
    public static final short RET             = ((short)169);
    public static final short TABLESWITCH     = ((short)170);
    public static final short LOOKUPSWITCH    = ((short)171);
    public static final short IRETURN         = ((short)172);
    public static final short LRETURN         = ((short)173);
    public static final short FRETURN         = ((short)174);
    public static final short DRETURN         = ((short)175);
    public static final short ARETURN         = ((short)176);
    public static final short RETURN          = ((short)177);
    public static final short GETSTATIC       = ((short)178);
    public static final short PUTSTATIC       = ((short)179);
    public static final short GETFIELD        = ((short)180);
    public static final short PUTFIELD        = ((short)181);
    public static final short INVOKEVIRTUAL   = ((short)182);
    public static final short INVOKESPECIAL   = ((short)183);
    public static final short INVOKESTATIC    = ((short)184);
    public static final short INVOKEINTERFACE = ((short)185);
    //public static final short               = ((short)186);
    public static final short NEW             = ((short)187);
    public static final short NEWARRAY        = ((short)188);
    public static final short ANEWARRAY       = ((short)189);
    public static final short ARRAYLENGTH     = ((short)190);
    public static final short ATHROW          = ((short)191);
    public static final short CHECKCAST       = ((short)192);
    public static final short INSTANCEOF      = ((short)193);
    public static final short MONITORENTER    = ((short)194);
    public static final short MONITOREXIT     = ((short)195);
    public static final short WIDE            = ((short)196);
    public static final short MULTIANEWARRAY  = ((short)197);
    public static final short IFNULL          = ((short)198);
    public static final short IFNONNULL       = ((short)199);
    public static final short GOTO_W          = ((short)200);
    public static final short JSR_W           = ((short)201);
    public static final short BREAKPOINT      = ((short)202);
    //public static final short               = ((short)203);
    //public static final short               = ((short)204);
    //public static final short               = ((short)205);
    //public static final short               = ((short)206);
    //public static final short               = ((short)207);
    //public static final short               = ((short)208);
    //public static final short RET_W         = ((short)209);
    //public static final short               = ((short)210);
    //public static final short               = ((short)211);
    //public static final short               = ((short)212);
    //public static final short               = ((short)213);
    //public static final short               = ((short)214);
    //public static final short               = ((short)215);
    //public static final short               = ((short)216);
    //public static final short               = ((short)217);
    //public static final short               = ((short)218);
    //public static final short               = ((short)219);
    //public static final short               = ((short)220);
    //public static final short               = ((short)221);
    //public static final short               = ((short)222);
    //public static final short               = ((short)223);
    //public static final short               = ((short)224);
    //public static final short               = ((short)225);
    //public static final short               = ((short)226);
    //public static final short               = ((short)227);
    //public static final short               = ((short)228);
    //public static final short               = ((short)229);
    //public static final short               = ((short)230);
    //public static final short               = ((short)231);
    //public static final short               = ((short)232);
    //public static final short               = ((short)233);
    //public static final short               = ((short)234);
    //public static final short               = ((short)235);
    //public static final short               = ((short)236);
    //public static final short               = ((short)237);
    //public static final short               = ((short)238);
    //public static final short               = ((short)239);
    //public static final short               = ((short)240);
    //public static final short               = ((short)241);
    //public static final short               = ((short)242);
    //public static final short               = ((short)243);
    //public static final short               = ((short)244);
    //public static final short               = ((short)245);
    //public static final short               = ((short)246);
    //public static final short               = ((short)247);
    //public static final short               = ((short)248);
    //public static final short               = ((short)249);
    //public static final short               = ((short)250);
    //public static final short               = ((short)251);
    //public static final short               = ((short)252);
    //public static final short               = ((short)253);
    public static final short IMPDEP1         = ((short)254);
    public static final short IMPDEP2         = ((short)255);

    private static final String[] OPCODE_NAMES = {
        "nop",         
        "aconst_null", 
        "iconst_m1",   
        "iconst_0",
        "iconst_1",
        "iconst_2",
        "iconst_3",
        "iconst_4",
        "iconst_5",
        "lconst_0",
        "lconst_1",
        "fconst_0",
        "fconst_1",
        "fconst_2",
        "dconst_0",
        "dconst_1",
        "bipush",
        "sipush",
        "ldc",
        "ldc_w",
        "ldc2_w",
        "iload",
        "lload",
        "fload",
        "dload",
        "aload",
        "iload_0",
        "iload_1",
        "iload_2",
        "iload_3",
        "lload_0",
        "lload_1",
        "lload_2",
        "lload_3",
        "fload_0",
        "fload_1",
        "fload_2",
        "fload_3",
        "dload_0",
        "dload_1",
        "dload_2",
        "dload_3",
        "aload_0",
        "aload_1",
        "aload_2",
        "aload_3",
        "iaload",
        "laload",
        "faload",
        "daload",
        "aaload",
        "baload",
        "caload",
        "saload",
        "istore",
        "lstore",
        "fstore",
        "dstore",
        "astore",
        "istore_0",
        "istore_1",
        "istore_2",
        "istore_3",
        "lstore_0",
        "lstore_1",
        "lstore_2",
        "lstore_3",
        "fstore_0",
        "fstore_1",
        "fstore_2",
        "fstore_3",
        "dstore_0",
        "dstore_1",
        "dstore_2",
        "dstore_3",
        "astore_0",
        "astore_1",
        "astore_2",
        "astore_3",
        "iastore",
        "lastore",
        "fastore",
        "dastore",
        "aastore",
        "bastore",
        "castore",
        "sastore",
        "pop",
        "pop2",
        "dup",
        "dup_x1",
        "dup_x2",
        "dup2",
        "dup2_x1",
        "dup2_x2",
        "swap",
        "iadd",
        "ladd",
        "fadd",
        "dadd",
        "isub",
        "lsub",
        "fsub",
        "dsub",
        "imul",
        "lmul",
        "fmul",
        "dmul",
        "idiv",
        "ldiv",
        "fdiv",
        "ddiv",
        "irem",
        "lrem",
        "frem",
        "drem",
        "ineg",
        "lneg",
        "fneg",
        "dneg",
        "ishl",
        "lshl",
        "ishr",
        "lshr",
        "iushr",
        "lushr",
        "iand",
        "land",
        "ior",
        "lor",
        "ixor",
        "lxor",
        "iinc",
        "i2l",
        "i2f",
        "i2d",
        "l2i",
        "l2f",
        "l2d",
        "f2i",
        "f2l",
        "f2d",
        "d2i",
        "d2l",
        "d2f",
        "i2b",
        "i2c",
        "i2s",
        "lcmp",
        "fcmpl",
        "fcmpg",
        "dcmpl",
        "dcmpg",
        "ifeq",
        "ifne",
        "iflt",
        "ifge",
        "ifgt",
        "ifle",
        "if_icmpeq",
        "if_icmpne",
        "if_icmplt",
        "if_icmpge",
        "if_icmpgt",
        "if_icmple",
        "if_acmpeq",
        "if_acmpne",
        "goto",
        "jsr",
        "ret",
        "tableswitch",
        "lookupswitch",
        "ireturn",
        "lreturn",
        "freturn",
        "dreturn",
        "areturn",
        "return",
        "getstatic",
        "putstatic",
        "getfield",
        "putfield",
        "invokevirtual",
        "invokespecial",
        "invokestatic",
        "invokeinterface",
        null, 
        "new",
        "newarray",
        "anewarray",
        "arraylength",
        "athrow",
        "checkcast",
        "instanceof",
        "monitorenter",
        "monitorexit",
        "wide",
        "multianewarray",
        "ifnull",
        "ifnonnull",
        "goto_w",
        "jsr_w",
        "breakpoint",
        null,
        null,
        null,
        null,
        null,
        null,
        "ret_w",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "impdep1",
        "impdep2"
    };

    private Instruction[] instructions;

    protected Code() {
        // Used by LazyCode
    }
    
    public Code(DataInput input, ConstantPool cp) {
        try {
            int length = input.readInt();
            this.parseCodeArray(length, input, cp);
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }

    public static String getOpcodeName(int opcode) {
        String rv = null;
        try {
            rv = Code.OPCODE_NAMES[opcode];
        } catch (ArrayIndexOutOfBoundsException e) {
            // Invalid opcode
        }

        return rv;
    }

    protected void parseCodeArray(int length, DataInput input, ConstantPool cp)
            throws IOException {
        Instruction head = null;
        Instruction prev_inst = null;
        int inst_count = 0;
        for (int offset = 0; offset < length; ) {
            short opcode = (short) (input.readUnsignedByte());
            Instruction inst = null;
            switch (opcode) {
                case NOP:
                    inst = new NopInstruction(offset);
                    break;
                case ACONST_NULL:
                    inst = new AconstNullInstruction(offset);
                    break;
                case ICONST_M1:
                    inst = new IconstM1Instruction(offset);
                    break;
                case ICONST_0:
                    inst = new Iconst0Instruction(offset);
                    break;
                case ICONST_1:
                    inst = new Iconst1Instruction(offset);
                    break;
                case ICONST_2:
                    inst = new Iconst2Instruction(offset);
                    break;
                case ICONST_3:
                    inst = new Iconst3Instruction(offset);
                    break;
                case ICONST_4:
                    inst = new Iconst4Instruction(offset);
                    break;
                case ICONST_5:
                    inst = new Iconst5Instruction(offset);
                    break;
                case LCONST_0:
                    inst = new Lconst0Instruction(offset);
                    break;
                case LCONST_1:
                    inst = new Lconst1Instruction(offset);
                    break;
                case FCONST_0:
                    inst = new Fconst0Instruction(offset);
                    break;
                case FCONST_1:
                    inst = new Fconst1Instruction(offset);
                    break;
                case FCONST_2:
                    inst = new Fconst2Instruction(offset);
                    break;
                case DCONST_0:
                    inst = new Dconst0Instruction(offset);
                    break;
                case DCONST_1:
                    inst = new Dconst1Instruction(offset);
                    break;
                case BIPUSH:
                    inst = new BipushInstruction(offset, input.readByte());
                    break;
                case SIPUSH:
                    inst = new SipushInstruction(offset, input.readShort());
                    break;
                case LDC:
                    inst = new LdcInstruction(offset, cp,
                            (short) input.readUnsignedByte());
                    break;
                case LDC_W:
                    inst = new LdcWInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case LDC2_W:
                    inst = new Ldc2WInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case ILOAD:
                    inst = new IloadInstruction(offset, 
                            (short) input.readUnsignedByte());
                    break;
                case LLOAD:
                    inst = new LloadInstruction(offset,
                            (short) input.readUnsignedByte());
                    break;
                case FLOAD:
                    inst = new FloadInstruction(offset,
                            (short) input.readUnsignedByte());
                    break;
                case DLOAD:
                    inst = new DloadInstruction(offset,
                            (short) input.readUnsignedByte());
                    break;
                case ALOAD:
                    inst = new AloadInstruction(offset,
                            (short) input.readUnsignedByte());
                    break;
                case ILOAD_0:
                    inst = new Iload0Instruction(offset);
                    break;
                case ILOAD_1:
                    inst = new Iload1Instruction(offset);
                    break;
                case ILOAD_2:
                    inst = new Iload2Instruction(offset);
                    break;
                case ILOAD_3:
                    inst = new Iload3Instruction(offset);
                    break;
                case LLOAD_0:
                    inst = new Lload0Instruction(offset);
                    break;
                case LLOAD_1:
                    inst = new Lload1Instruction(offset);
                    break;
                case LLOAD_2:
                    inst = new Lload2Instruction(offset);
                    break;
                case LLOAD_3:
                    inst = new Lload3Instruction(offset);
                    break;
                case FLOAD_0:
                    inst = new Fload0Instruction(offset);
                    break;
                case FLOAD_1:
                    inst = new Fload1Instruction(offset);
                    break;
                case FLOAD_2:
                    inst = new Fload2Instruction(offset);
                    break;
                case FLOAD_3:
                    inst = new Fload3Instruction(offset);
                    break;
                case DLOAD_0:
                    inst = new Dload0Instruction(offset);
                    break;
                case DLOAD_1:
                    inst = new Dload1Instruction(offset);
                    break;
                case DLOAD_2:
                    inst = new Dload2Instruction(offset);
                    break;
                case DLOAD_3:
                    inst = new Dload3Instruction(offset);
                    break;
                case ALOAD_0:
                    inst = new Aload0Instruction(offset);
                    break;
                case ALOAD_1:
                    inst = new Aload1Instruction(offset);
                    break;
                case ALOAD_2:
                    inst = new Aload2Instruction(offset);
                    break;
                case ALOAD_3:
                    inst = new Aload3Instruction(offset);
                    break;
                case IALOAD:
                    inst = new IaloadInstruction(offset);
                    break;
                case LALOAD:
                    inst = new LaloadInstruction(offset);
                    break;
                case FALOAD:
                    inst = new FaloadInstruction(offset);
                    break;
                case DALOAD:
                    inst = new DaloadInstruction(offset);
                    break;
                case AALOAD:
                    inst = new AaloadInstruction(offset);
                    break;
                case BALOAD:
                    inst = new BaloadInstruction(offset);
                    break;
                case CALOAD:
                    inst = new CaloadInstruction(offset);
                    break;
                case SALOAD:
                    inst = new SaloadInstruction(offset);
                    break;
                case ISTORE:
                    inst = new IstoreInstruction(offset,
                            (short) input.readUnsignedByte());
                    break;
                case LSTORE:
                    inst = new LstoreInstruction(offset,
                            (short) input.readUnsignedByte());
                    break;
                case FSTORE:
                    inst = new FstoreInstruction(offset,
                            (short) input.readUnsignedByte());
                    break;
                case DSTORE:
                    inst = new DstoreInstruction(offset,
                            (short) input.readUnsignedByte());
                    break;
                case ASTORE:
                    inst = new AstoreInstruction(offset,
                            (short) input.readUnsignedByte());
                    break;
                case ISTORE_0:
                    inst = new Istore0Instruction(offset);
                    break;
                case ISTORE_1:
                    inst = new Istore1Instruction(offset);
                    break;
                case ISTORE_2:
                    inst = new Istore2Instruction(offset);
                    break;
                case ISTORE_3:
                    inst = new Istore3Instruction(offset);
                    break;
                case LSTORE_0:
                    inst = new Lstore0Instruction(offset);
                    break;
                case LSTORE_1:
                    inst = new Lstore1Instruction(offset);
                    break;
                case LSTORE_2:
                    inst = new Lstore2Instruction(offset);
                    break;
                case LSTORE_3:
                    inst = new Lstore3Instruction(offset);
                    break;
                case FSTORE_0:
                    inst = new Fstore0Instruction(offset);
                    break;
                case FSTORE_1:
                    inst = new Fstore1Instruction(offset);
                    break;
                case FSTORE_2:
                    inst = new Fstore2Instruction(offset);
                    break;
                case FSTORE_3:
                    inst = new Fstore3Instruction(offset);
                    break;
                case DSTORE_0:
                    inst = new Dstore0Instruction(offset);
                    break;
                case DSTORE_1:
                    inst = new Dstore1Instruction(offset);
                    break;
                case DSTORE_2:
                    inst = new Dstore2Instruction(offset);
                    break;
                case DSTORE_3:
                    inst = new Dstore3Instruction(offset);
                    break;
                case ASTORE_0:
                    inst = new Astore0Instruction(offset);
                    break;
                case ASTORE_1:
                    inst = new Astore1Instruction(offset);
                    break;
                case ASTORE_2:
                    inst = new Astore2Instruction(offset);
                    break;
                case ASTORE_3:
                    inst = new Astore3Instruction(offset);
                    break;
                case IASTORE:
                    inst = new IastoreInstruction(offset);
                    break;
                case LASTORE:
                    inst = new LastoreInstruction(offset);
                    break;
                case FASTORE:
                    inst = new FastoreInstruction(offset);
                    break;
                case DASTORE:
                    inst = new DastoreInstruction(offset);
                    break;
                case AASTORE:
                    inst = new AastoreInstruction(offset);
                    break;
                case BASTORE:
                    inst = new BastoreInstruction(offset);
                    break;
                case CASTORE:
                    inst = new CastoreInstruction(offset);
                    break;
                case SASTORE:
                    inst = new SastoreInstruction(offset);
                    break;
                case POP:
                    inst = new PopInstruction(offset);
                    break;
                case POP2:
                    inst = new Pop2Instruction(offset);
                    break;
                case DUP:
                    inst = new DupInstruction(offset);
                    break;
                case DUP_X1:
                    inst = new DupX1Instruction(offset);
                    break;
                case DUP_X2:
                    inst = new DupX2Instruction(offset);
                    break;
                case DUP2:
                    inst = new Dup2Instruction(offset);
                    break;
                case DUP2_X1:
                    inst = new Dup2X1Instruction(offset);
                    break;
                case DUP2_X2:
                    inst = new Dup2X2Instruction(offset);
                    break;
                case SWAP:
                    inst = new SwapInstruction(offset);
                    break;
                case IADD:
                    inst = new IaddInstruction(offset);
                    break;
                case LADD:
                    inst = new LaddInstruction(offset);
                    break;
                case FADD:
                    inst = new FaddInstruction(offset);
                    break;
                case DADD:
                    inst = new DaddInstruction(offset);
                    break;
                case ISUB:
                    inst = new IsubInstruction(offset);
                    break;
                case LSUB:
                    inst = new LsubInstruction(offset);
                    break;
                case FSUB:
                    inst = new FsubInstruction(offset);
                    break;
                case DSUB:
                    inst = new DsubInstruction(offset);
                    break;
                case IMUL:
                    inst = new ImulInstruction(offset);
                    break;
                case LMUL:
                    inst = new LmulInstruction(offset);
                    break;
                case FMUL:
                    inst = new FmulInstruction(offset);
                    break;
                case DMUL:
                    inst = new DmulInstruction(offset);
                    break;
                case IDIV:
                    inst = new IdivInstruction(offset);
                    break;
                case LDIV:
                    inst = new LdivInstruction(offset);
                    break;
                case FDIV:
                    inst = new FdivInstruction(offset);
                    break;
                case DDIV:
                    inst = new DdivInstruction(offset);
                    break;
                case IREM:
                    inst = new IremInstruction(offset);
                    break;
                case LREM:
                    inst = new LremInstruction(offset);
                    break;
                case FREM:
                    inst = new FremInstruction(offset);
                    break;
                case DREM:
                    inst = new DremInstruction(offset);
                    break;
                case INEG:
                    inst = new InegInstruction(offset);
                    break;
                case LNEG:
                    inst = new LnegInstruction(offset);
                    break;
                case FNEG:
                    inst = new FnegInstruction(offset);
                    break;
                case DNEG:
                    inst = new DnegInstruction(offset);
                    break;
                case ISHL:
                    inst = new IshlInstruction(offset);
                    break;
                case LSHL:
                    inst = new LshlInstruction(offset);
                    break;
                case ISHR:
                    inst = new IshrInstruction(offset);
                    break;
                case LSHR:
                    inst = new LshrInstruction(offset);
                    break;
                case IUSHR:
                    inst = new IushrInstruction(offset);
                    break;
                case LUSHR:
                    inst = new LushrInstruction(offset);
                    break;
                case IAND:
                    inst = new IandInstruction(offset);
                    break;
                case LAND:
                    inst = new LandInstruction(offset);
                    break;
                case IOR:
                    inst = new IorInstruction(offset);
                    break;
                case LOR:
                    inst = new LorInstruction(offset);
                    break;
                case IXOR:
                    inst = new IxorInstruction(offset);
                    break;
                case LXOR:
                    inst = new LxorInstruction(offset);
                    break;
                case IINC:
                    inst = new IincInstruction(offset,
                            (short) input.readUnsignedByte(), input.readByte());
                    break;
                case I2L:
                    inst = new I2lInstruction(offset);
                    break;
                case I2F:
                    inst = new I2fInstruction(offset);
                    break;
                case I2D:
                    inst = new I2dInstruction(offset);
                    break;
                case L2I:
                    inst = new L2iInstruction(offset);
                    break;
                case L2F:
                    inst = new L2fInstruction(offset);
                    break;
                case L2D:
                    inst = new L2dInstruction(offset);
                    break;
                case F2I:
                    inst = new F2iInstruction(offset);
                    break;
                case F2L:
                    inst = new F2lInstruction(offset);
                    break;
                case F2D:
                    inst = new F2dInstruction(offset);
                    break;
                case D2I:
                    inst = new D2iInstruction(offset);
                    break;
                case D2L:
                    inst = new D2lInstruction(offset);
                    break;
                case D2F:
                    inst = new D2fInstruction(offset);
                    break;
                case I2B:
                    inst = new I2bInstruction(offset);
                    break;
                case I2C:
                    inst = new I2cInstruction(offset);
                    break;
                case I2S:
                    inst = new I2sInstruction(offset);
                    break;
                case LCMP:
                    inst = new LcmpInstruction(offset);
                    break;
                case FCMPL:
                    inst = new FcmplInstruction(offset);
                    break;
                case FCMPG:
                    inst = new FcmpgInstruction(offset);
                    break;
                case DCMPL:
                    inst = new DcmplInstruction(offset);
                    break;
                case DCMPG:
                    inst = new DcmpgInstruction(offset);
                    break;
                case IFEQ:
                    inst = new IfeqInstruction(offset, input.readShort());
                    break;
                case IFNE:
                    inst = new IfneInstruction(offset, input.readShort());
                    break;
                case IFLT:
                    inst = new IfltInstruction(offset, input.readShort());
                    break;
                case IFGE:
                    inst = new IfgeInstruction(offset, input.readShort());
                    break;
                case IFGT:
                    inst = new IfgtInstruction(offset, input.readShort());
                    break;
                case IFLE:
                    inst = new IfleInstruction(offset, input.readShort());
                    break;
                case IF_ICMPEQ:
                    inst = new IfIcmpeqInstruction(offset, input.readShort());
                    break;
                case IF_ICMPNE:
                    inst = new IfIcmpneInstruction(offset, input.readShort());
                    break;
                case IF_ICMPLT:
                    inst = new IfIcmpltInstruction(offset, input.readShort());
                    break;
                case IF_ICMPGE:
                    inst = new IfIcmpgeInstruction(offset, input.readShort());
                    break;
                case IF_ICMPGT:
                    inst = new IfIcmpgtInstruction(offset, input.readShort());
                    break;
                case IF_ICMPLE:
                    inst = new IfIcmpleInstruction(offset, input.readShort());
                    break;
                case IF_ACMPEQ:
                    inst = new IfAcmpeqInstruction(offset, input.readShort());
                    break;
                case IF_ACMPNE:
                    inst = new IfAcmpneInstruction(offset, input.readShort());
                    break;
                case GOTO:
                    inst = new GotoInstruction(offset, input.readShort());
                    break;
                case JSR:
                    inst = new JsrInstruction(offset, input.readShort());
                    break;
                case RET:
                    inst = new RetInstruction(offset,
                            (short) input.readUnsignedByte());
                    break;
                case TABLESWITCH:
                    {
                        int pad = 3 - (offset % 4);
                        for (int i = 0; i < pad; i++) {
                            input.readByte(); // Skip byte
                        }
                        int default_index = input.readInt();
                        int low = input.readInt();
                        int high = input.readInt();
                        int count = high - low + 1;
                        int[] values = null;
                        int[] offsets = null;
                        if (count > 0) {
                            values = new int[count];
                            offsets = new int[count];
                            for (int i = 0; i < count; i++) {
                                values[i] = low + i;
                                offsets[i] = input.readInt();
                            }
                        }
                        inst = new TableswitchInstruction(offset, (byte) pad,
                                default_index, low, high, values, offsets);
                    }
                    break;
                case LOOKUPSWITCH:
                    {
                        int pad = 3 - (offset % 4);
                        for (int i = 0; i < pad; i++) {
                            input.readByte(); // Skip byte
                        }
                        int default_index = input.readInt();
                        int count = input.readInt();
                        int[] values = null;
                        int[] offsets = null;
                        if (count > 0) {
                            values = new int[count];
                            offsets = new int[count];
                            for (int i = 0; i < count; i++) {
                                values[i] = input.readInt();
                                offsets[i] = input.readInt();
                            }
                        }
                        inst = new LookupswitchInstruction(offset, (byte) pad,
                                default_index, count, values, offsets);
                    }
                    break;
                case IRETURN:
                    inst = new IreturnInstruction(offset);
                    break;
                case LRETURN:
                    inst = new LreturnInstruction(offset);
                    break;
                case FRETURN:
                    inst = new FreturnInstruction(offset);
                    break;
                case DRETURN:
                    inst = new DreturnInstruction(offset);
                    break;
                case ARETURN:
                    inst = new AreturnInstruction(offset);
                    break;
                case RETURN:
                    inst = new ReturnInstruction(offset);
                    break;
                case GETSTATIC:
                    inst = new GetstaticInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case PUTSTATIC:
                    inst = new PutstaticInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case GETFIELD:
                    inst = new GetfieldInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case PUTFIELD:
                    inst = new PutfieldInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case INVOKEVIRTUAL:
                    inst = new InvokevirtualInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case INVOKESPECIAL:
                    inst = new InvokespecialInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case INVOKESTATIC:
                    inst = new InvokestaticInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case INVOKEINTERFACE:
                    inst = new InvokeinterfaceInstruction(offset, cp,
                            input.readUnsignedShort(),
                            (short) input.readUnsignedByte());
                    input.readByte(); // Skip '0' byte
                    break;
                case NEW:
                    inst = new NewInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case NEWARRAY:
                    inst = new NewarrayInstruction(offset, input.readByte());
                    break;
                case ANEWARRAY:
                    inst = new AnewarrayInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case ARRAYLENGTH:
                    inst = new ArraylengthInstruction(offset);
                    break;
                case ATHROW:
                    inst = new AthrowInstruction(offset);
                    break;
                case CHECKCAST:
                    inst = new CheckcastInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case INSTANCEOF:
                    inst = new InstanceofInstruction(offset, cp,
                            input.readUnsignedShort());
                    break;
                case MONITORENTER:
                    inst = new MonitorEnterInstruction(offset);
                    break;
                case MONITOREXIT:
                    inst = new MonitorExitInstruction(offset);
                    break;
                case WIDE:
                    {
                        short modified_opcode = (short) input.readUnsignedByte();
                        int index = input.readUnsignedShort();
                        if (modified_opcode == IINC) {
                            inst = new WideIincInstruction(offset, index,
                                    input.readShort());
                        } else {
                            inst = new WideInstruction(offset, modified_opcode,
                                    index);
                        }
                    }
                    break;
                case MULTIANEWARRAY:
                    inst = new MultianewarrayInstruction(offset, cp,
                            input.readUnsignedShort(),
                            (short) input.readUnsignedByte());
                    break;
                case IFNULL:
                    inst = new IfnullInstruction(offset, input.readShort());
                    break;
                case IFNONNULL:
                    inst = new IfnonnullInstruction(offset, input.readShort());
                    break;
                case GOTO_W:
                    inst = new GotoWInstruction(offset, input.readInt());
                    break;
                case JSR_W:
                    inst = new JsrWInstruction(offset, input.readInt());
                    break;
                case BREAKPOINT:
                    // Ignore
                    break;
                default:
                    // Should not happen
                    throw new ClassFileFormatException("Unknown opcode: " + opcode);
            }
            if (inst != null) {
                offset += inst.getLength();
                if (prev_inst != null) {
                    prev_inst.setNext(inst);
                } else {
                    head = inst; // Remember 1st inst
                }
                inst.setPrev(prev_inst);
                prev_inst = inst;
                inst_count += 1;
            } else {
                offset += 1; // BREAKPOINT bytecode parsed
            }

            
        }

        if (inst_count > 0) {
            Instruction[] insts = new Instruction[inst_count];
            for (int i = 0; i < inst_count; i++) {
                insts[i] = head;
                head = head.getNext();
            }
            this.instructions = insts;
            this.resolveBranches();
        }
    }

    protected void resolveBranches() {
        Instruction[] insts = this.instructions;
        int inst_count = insts.length;
        for (int i = 0; i < inst_count; i++) {
            Instruction inst = insts[i];
            if (inst instanceof BranchInstruction) {
                BranchInstruction branch = (BranchInstruction) inst;
                branch.setTarget(this.lookup(branch.getTargetOffset()));
            }
        }
    }

    public boolean isConcrete() {
        return true; // Overriden in LazyCode
    }
    
    public Instruction[] getInstructions() {
        if (this.instructions != null) {
            return (Instruction[]) this.instructions.clone();
        }
        return null;
    }

    public Instruction lookup(int offset) {
        Instruction[] insts = this.instructions;

        if (insts != null) {            
            int low = 0;
            int high = insts.length;
            while (low <= high) {
                int mid = (low + high) / 2;
                Instruction i = insts[mid];
                int candidate = i.getOffset();
                if (candidate > offset) {
                    high = mid - 1;
                } else if (candidate < offset) {
                    low = mid + 1;
                } else {
                    return i;
                }
            }
        }

        return null;
        
    }

    public static short toUnsignedByte(byte b) {
        return (short) (b & 0x00FF);
    }

    public static short parseShort(byte[] bytes, int index) {
        int b1 = ((int) bytes[index]) & 0x000000FF;
        int b2 = ((int) bytes[index + 1]) & 0x000000FF;
        return (short) ((b1 << 8) | b2);
    }

    public static int parseInt(byte[] bytes, int index) {
        int b1 = ((int) bytes[index]) & 0x000000FF;
        int b2 = ((int) bytes[index + 1]) & 0x000000FF;
        int b3 = ((int) bytes[index + 2]) & 0x000000FF;
        int b4 = ((int) bytes[index + 3]) & 0x000000FF;
        return ((b1 << 24) | (b2 << 16) | (b3 << 8) | b4);
    }
    
    public int getSize() {
        return this.instructions.length;
    }

    public String toString() {
        String rv = "";
        if (this.instructions != null) {
            for (int i = 0; i < this.instructions.length; i++) {
                rv += this.instructions[i].toString() + "\n";
            }
        }

        return rv;
    }
}
