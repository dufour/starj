package starj.coffer;

public class LshrInstruction extends BitwiseInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lshr";

    public LshrInstruction(int offset) {
        super(Code.LSHR, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

