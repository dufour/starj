package starj.coffer;

public class DremInstruction extends Instruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "drem";

    public DremInstruction(int offset) {
        super(Code.DREM, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

