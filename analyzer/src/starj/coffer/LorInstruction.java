package starj.coffer;

public class LorInstruction extends BooleanInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lor";

    public LorInstruction(int offset) {
        super(Code.LOR, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

