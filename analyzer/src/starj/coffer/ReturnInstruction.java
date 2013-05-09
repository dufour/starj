package starj.coffer;

public class ReturnInstruction extends ReturningInstruction {
    public static final String OPCODE_NAME = "return";

    public ReturnInstruction(int offset) {
        super(Code.RETURN, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
