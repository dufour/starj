package starj.coffer;

public class L2fInstruction extends Instruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "l2f";

    public L2fInstruction(int offset) {
        super(Code.L2F, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

