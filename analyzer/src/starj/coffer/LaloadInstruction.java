package starj.coffer;

public class LaloadInstruction extends ArrayInstruction
        implements LongInstruction {
    public static final String OPCODE_NAME = "laload";

    public LaloadInstruction(int offset) {
        super(Code.LALOAD, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
