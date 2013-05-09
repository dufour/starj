package starj.coffer;

public class F2iInstruction extends Instruction
        implements FloatInstruction {
    public static final String OPCODE_NAME = "f2i";

    public F2iInstruction(int offset) {
        super(Code.F2I, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

