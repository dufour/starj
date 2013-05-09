package starj.coffer;

public class FmulInstruction extends Instruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fmul";

    public FmulInstruction(int offset) {
        super(Code.FMUL, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

