package starj.coffer;

public class IreturnInstruction extends ReturningInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ireturn";

    public IreturnInstruction(int offset) {
        super(Code.IRETURN, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

