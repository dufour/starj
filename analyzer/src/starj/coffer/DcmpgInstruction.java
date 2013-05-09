package starj.coffer;

public class DcmpgInstruction extends Instruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dcmpg";

    public DcmpgInstruction(int offset) {
        super(Code.DCMPG, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

