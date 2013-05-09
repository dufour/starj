package starj.coffer;

public class IushrInstruction extends BitwiseInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "iushr";

    public IushrInstruction(int offset) {
        super(Code.IUSHR, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

