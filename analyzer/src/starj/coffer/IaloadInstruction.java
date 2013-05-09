package starj.coffer;

public class IaloadInstruction extends ArrayInstruction
        implements IntInstruction {
    public static final String OPCODE_NAME = "iaload";

    public IaloadInstruction(int offset) {
        super(Code.IALOAD, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
