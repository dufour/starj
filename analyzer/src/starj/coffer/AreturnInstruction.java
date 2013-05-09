package starj.coffer;

public class AreturnInstruction extends ReturningInstruction {
    public static final String OPCODE_NAME = "areturn";

    public AreturnInstruction(int offset) {
        super(Code.ARETURN, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
