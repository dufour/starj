package starj.coffer;

public class D2fInstruction extends Instruction
        implements DoubleInstruction {
    public static final String OPCODE_NAME = "d2f";

    public D2fInstruction(int offset) {
        super(Code.D2F, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

