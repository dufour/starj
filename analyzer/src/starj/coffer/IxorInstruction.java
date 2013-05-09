package starj.coffer;

public class IxorInstruction extends BooleanInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ixor";

    public IxorInstruction(int offset) {
        super(Code.IXOR, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

