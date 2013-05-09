package starj.coffer;

public class DastoreInstruction extends ArrayInstruction
        implements DoubleInstruction {
    public static final String OPCODE_NAME = "dastore";

    public DastoreInstruction(int offset) {
        super(Code.DASTORE, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
