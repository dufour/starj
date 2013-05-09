package starj.coffer;

public class AloadInstruction extends LoadInstruction {
    public static final String OPCODE_NAME = "aload";

    public AloadInstruction(int offset, short index) {
        super(Code.ALOAD, 2, offset, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
