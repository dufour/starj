package starj.coffer;

public class LloadInstruction extends LoadInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lload";

    public LloadInstruction(int offset, short index) {
        super(Code.LLOAD, 2, offset, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
