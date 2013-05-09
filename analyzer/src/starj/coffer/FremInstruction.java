package starj.coffer;

public class FremInstruction extends Instruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "frem";

    public FremInstruction(int offset) {
        super(Code.FREM, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

