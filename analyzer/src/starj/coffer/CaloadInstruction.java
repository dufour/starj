package starj.coffer;

public class CaloadInstruction extends ArrayInstruction
        implements CharInstruction {
    public static final String OPCODE_NAME = "caload";

    public CaloadInstruction(int offset) {
        super(Code.CALOAD, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

