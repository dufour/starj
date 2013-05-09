package starj.coffer;

public class CheckcastInstruction extends ConstantPoolInstruction {
    public static final String OPCODE_NAME = "checkcast";

    public CheckcastInstruction(int offset, ConstantPool cp, int index) {
        super(Code.CHECKCAST, 3, offset, cp, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

