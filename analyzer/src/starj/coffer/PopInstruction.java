package starj.coffer;

public class PopInstruction extends Instruction {
    public static final String OPCODE_NAME = "pop";

    public PopInstruction(int offset) {
        super(Code.POP, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

