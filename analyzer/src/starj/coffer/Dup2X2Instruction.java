package starj.coffer;

public class Dup2X2Instruction extends Instruction {
    public static final String OPCODE_NAME = "dup2_x2";

    public Dup2X2Instruction(int offset) {
        super(Code.DUP2_X2, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

