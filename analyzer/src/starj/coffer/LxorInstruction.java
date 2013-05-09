package starj.coffer;

public class LxorInstruction extends BooleanInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lxor";

    public LxorInstruction(int offset) {
        super(Code.LXOR, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

