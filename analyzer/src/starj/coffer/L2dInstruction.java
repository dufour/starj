package starj.coffer;

public class L2dInstruction extends Instruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "l2d";

    public L2dInstruction(int offset) {
        super(Code.L2D, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

