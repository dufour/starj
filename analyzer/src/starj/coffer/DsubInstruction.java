package starj.coffer;

public class DsubInstruction extends Instruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dsub";

    public DsubInstruction(int offset) {
        super(Code.DSUB, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

