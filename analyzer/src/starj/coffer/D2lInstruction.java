package starj.coffer;

public class D2lInstruction extends Instruction
        implements DoubleInstruction {
    public static final String OPCODE_NAME = "d2l";

    public D2lInstruction(int offset) {
        super(Code.D2L, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

