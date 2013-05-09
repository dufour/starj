package starj.coffer;

public class LushrInstruction extends BitwiseInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lushr";

    public LushrInstruction(int offset) {
        super(Code.LUSHR, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

