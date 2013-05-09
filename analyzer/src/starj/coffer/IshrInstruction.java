package starj.coffer;

public class IshrInstruction extends BitwiseInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ishr";

    public IshrInstruction(int offset) {
        super(Code.ISHR, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

