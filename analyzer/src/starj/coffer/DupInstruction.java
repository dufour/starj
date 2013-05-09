package starj.coffer;

public class DupInstruction extends Instruction {
    public static final String OPCODE_NAME = "dup";

    public DupInstruction(int offset) {
        super(Code.DUP, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

