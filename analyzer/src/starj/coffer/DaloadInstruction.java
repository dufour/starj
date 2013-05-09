package starj.coffer;

public class DaloadInstruction extends ArrayInstruction
        implements DoubleInstruction {
    public static final String OPCODE_NAME = "daload";

    public DaloadInstruction(int offset) {
        super(Code.DALOAD, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
