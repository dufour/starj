package starj.coffer;

public class FcmpgInstruction extends Instruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fcmpg";

    public FcmpgInstruction(int offset) {
        super(Code.FCMPG, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

