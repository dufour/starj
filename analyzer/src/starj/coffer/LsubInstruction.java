package starj.coffer;

public class LsubInstruction extends Instruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lsub";

    public LsubInstruction(int offset) {
        super(Code.LSUB, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

