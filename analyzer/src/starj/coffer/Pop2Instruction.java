package starj.coffer;

public class Pop2Instruction extends Instruction {
    public static final String OPCODE_NAME = "pop2";

    public Pop2Instruction(int offset) {
        super(Code.POP2, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

