package starj.coffer;

public class Dup2Instruction extends Instruction {
    public static final String OPCODE_NAME = "dup2";

    public Dup2Instruction(int offset) {
        super(Code.DUP2, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

