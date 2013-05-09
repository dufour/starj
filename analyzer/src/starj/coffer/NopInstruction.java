package starj.coffer;

public class NopInstruction extends Instruction {
    public static final String OPCODE_NAME = "nop";

    public NopInstruction(int offset) {
        super(Code.NOP, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

