package starj.coffer;

public class FnegInstruction extends Instruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fneg";

    public FnegInstruction(int offset) {
        super(Code.FNEG, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

