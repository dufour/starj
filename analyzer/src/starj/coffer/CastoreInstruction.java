package starj.coffer;

public class CastoreInstruction extends ArrayInstruction
        implements CharInstruction {
    public static final String OPCODE_NAME = "castore";

    public CastoreInstruction(int offset) {
        super(Code.CASTORE, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

