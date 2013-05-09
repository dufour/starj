package starj.coffer;

public class LreturnInstruction extends ReturningInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lreturn";

    public LreturnInstruction(int offset) {
        super(Code.LRETURN, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

