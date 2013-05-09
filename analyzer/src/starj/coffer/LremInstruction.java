package starj.coffer;

public class LremInstruction extends Instruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lrem";

    public LremInstruction(int offset) {
        super(Code.LREM, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

