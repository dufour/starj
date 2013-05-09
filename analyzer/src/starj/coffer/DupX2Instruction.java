package starj.coffer;

public class DupX2Instruction extends Instruction {
    public static final String OPCODE_NAME = "dup_x2";

    public DupX2Instruction(int offset) {
        super(Code.DUP_X2, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

