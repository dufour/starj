package starj.coffer;

public class InegInstruction extends Instruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ineg";

    public InegInstruction(int offset) {
        super(Code.INEG, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

