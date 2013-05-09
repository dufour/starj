package starj.coffer;

public class DreturnInstruction extends ReturningInstruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dreturn";

    public DreturnInstruction(int offset) {
        super(Code.DRETURN, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

