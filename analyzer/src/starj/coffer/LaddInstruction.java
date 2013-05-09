package starj.coffer;

public class LaddInstruction extends Instruction
        implements LongInstruction {
    public static final String OPCODE_NAME = "ladd";

    public LaddInstruction(int offset) {
        super(Code.LADD, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

