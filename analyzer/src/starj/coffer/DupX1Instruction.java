package starj.coffer;

public class DupX1Instruction extends Instruction {
    public static final String OPCODE_NAME = "dup_x1";

    public DupX1Instruction(int offset) {
        super(Code.DUP_X1, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

