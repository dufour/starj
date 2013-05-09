package starj.coffer;

public class WideInstruction extends Instruction {
    private short modified_opcode;
    private int index;

    public WideInstruction(int offset, short modified_opcode, int index) {
        super(Code.WIDE, (modified_opcode == Code.IINC ? 6 : 4), offset);
        this.modified_opcode = modified_opcode;
        this.index = index;
    }

    public short getModifiedOpcode() {
        return this.modified_opcode;
    }

    public int getIndex() {
        return this.index;
    }

    public String getOpcodeName() {
        String modified_opcode_name = null;
        switch (this.modified_opcode) {
            case Code.ILOAD:
                modified_opcode_name = IloadInstruction.OPCODE_NAME;
                break;
            case Code.FLOAD:
                modified_opcode_name = FloadInstruction.OPCODE_NAME;
                break;
            case Code.ALOAD:
                modified_opcode_name = AloadInstruction.OPCODE_NAME;
                break;
            case Code.LLOAD:
                modified_opcode_name = LloadInstruction.OPCODE_NAME;
                break;
            case Code.DLOAD:
                modified_opcode_name = DloadInstruction.OPCODE_NAME;
                break;
            case Code.ISTORE:
                modified_opcode_name = IstoreInstruction.OPCODE_NAME;
                break;
            case Code.FSTORE:
                modified_opcode_name = FstoreInstruction.OPCODE_NAME;
                break;
            case Code.ASTORE:
                modified_opcode_name = AstoreInstruction.OPCODE_NAME;
                break;
            case Code.LSTORE:
                modified_opcode_name = LstoreInstruction.OPCODE_NAME;
                break;
            case Code.DSTORE:
                modified_opcode_name = DstoreInstruction.OPCODE_NAME;
                break;
            case Code.RET:
                modified_opcode_name = RetInstruction.OPCODE_NAME;
                break;
            case Code.IINC:
                modified_opcode_name = IincInstruction.OPCODE_NAME;
                break;
            default:
                throw new ClassFileFormatException("Invalid modified opcode:" 
                        + this.modified_opcode);
        }

        return "wide " + modified_opcode_name;
    }
}

