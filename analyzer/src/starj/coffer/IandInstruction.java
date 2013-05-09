package starj.coffer;

public class IandInstruction extends BooleanInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "iand";

    public IandInstruction(int offset) {
        super(Code.IAND, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

