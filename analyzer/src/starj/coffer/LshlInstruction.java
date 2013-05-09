package starj.coffer;

public class LshlInstruction extends BitwiseInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lshl";

    public LshlInstruction(int offset) {
        super(Code.LSHL, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

