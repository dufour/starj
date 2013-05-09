package starj.coffer;

public class Dup2X1Instruction extends Instruction {
    public static final String OPCODE_NAME = "dup2_x1";

    public Dup2X1Instruction(int offset) {
        super(Code.DUP2_X1, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

