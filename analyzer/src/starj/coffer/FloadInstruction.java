package starj.coffer;

public class FloadInstruction extends LoadInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fload";

    public FloadInstruction(int offset, short index) {
        super(Code.FLOAD, 2, offset, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
