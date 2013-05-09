package starj.coffer;

public class IshlInstruction extends BitwiseInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ishl";

    public IshlInstruction(int offset) {
        super(Code.ISHL, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

