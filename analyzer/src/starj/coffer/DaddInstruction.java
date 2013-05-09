package starj.coffer;

public class DaddInstruction extends Instruction
        implements DoubleInstruction {
    public static final String OPCODE_NAME = "dadd";

    public DaddInstruction(int offset) {
        super(Code.DADD, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

