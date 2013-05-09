package starj.coffer;

public class F2dInstruction extends Instruction
        implements FloatInstruction {
    public static final String OPCODE_NAME = "f2d";

    public F2dInstruction(int offset) {
        super(Code.F2D, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

