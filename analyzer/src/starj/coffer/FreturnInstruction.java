package starj.coffer;

public class FreturnInstruction extends ReturningInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "freturn";

    public FreturnInstruction(int offset) {
        super(Code.FRETURN, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

