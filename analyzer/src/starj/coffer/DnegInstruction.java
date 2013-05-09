package starj.coffer;

public class DnegInstruction extends Instruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dneg";

    public DnegInstruction(int offset) {
        super(Code.DNEG, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

