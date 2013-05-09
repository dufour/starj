package starj.coffer;

public class FaloadInstruction extends ArrayInstruction
        implements FloatInstruction {
    public static final String OPCODE_NAME = "faload";

    public FaloadInstruction(int offset) {
        super(Code.FALOAD, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
