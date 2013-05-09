package starj.coffer;

public class L2iInstruction extends Instruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "l2i";

    public L2iInstruction(int offset) {
        super(Code.L2I, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

