package starj.coffer;

public class LnegInstruction extends Instruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lneg";

    public LnegInstruction(int offset) {
        super(Code.LNEG, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

