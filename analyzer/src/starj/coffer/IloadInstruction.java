package starj.coffer;

public class IloadInstruction extends LoadInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "iload";

    public IloadInstruction(int offset, short index) {
        super(Code.ILOAD, 2, offset, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
