package starj.coffer;

public class FaddInstruction extends Instruction
        implements FloatInstruction {
    public static final String OPCODE_NAME = "fadd";

    public FaddInstruction(int offset) {
        super(Code.FADD, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

