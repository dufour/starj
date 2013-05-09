package starj.coffer;

public class D2iInstruction extends Instruction
        implements DoubleInstruction {
    public static final String OPCODE_NAME = "d2i";

    public D2iInstruction(int offset) {
        super(Code.D2I, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

