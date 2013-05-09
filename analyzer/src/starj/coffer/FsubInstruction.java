package starj.coffer;

public class FsubInstruction extends Instruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fsub";

    public FsubInstruction(int offset) {
        super(Code.FSUB, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

