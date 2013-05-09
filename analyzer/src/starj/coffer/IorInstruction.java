package starj.coffer;

public class IorInstruction extends BooleanInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ior";

    public IorInstruction(int offset) {
        super(Code.IOR, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

