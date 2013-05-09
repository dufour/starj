package starj.coffer;

public class DloadInstruction extends LoadInstruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dload";

    public DloadInstruction(int offset, short index) {
        super(Code.DLOAD, 2, offset, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
