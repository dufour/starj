package starj.coffer;

public class LandInstruction extends BooleanInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "land";

    public LandInstruction(int offset) {
        super(Code.LAND, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

