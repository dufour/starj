package starj.coffer;

public class LmulInstruction extends Instruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lmul";

    public LmulInstruction(int offset) {
        super(Code.LMUL, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

