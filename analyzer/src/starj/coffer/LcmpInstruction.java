package starj.coffer;

public class LcmpInstruction extends Instruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lcmp";

    public LcmpInstruction(int offset) {
        super(Code.LCMP, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

