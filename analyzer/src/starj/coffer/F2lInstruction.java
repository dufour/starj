package starj.coffer;

public class F2lInstruction extends Instruction
        implements FloatInstruction {
    public static final String OPCODE_NAME = "f2l";

    public F2lInstruction(int offset) {
        super(Code.F2L, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

