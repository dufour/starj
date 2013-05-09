package starj.coffer;

public class LastoreInstruction extends ArrayInstruction
        implements LongInstruction {
    public static final String OPCODE_NAME = "lastore";

    public LastoreInstruction(int offset) {
        super(Code.LASTORE, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
