package starj.coffer;

public class AconstNullInstruction extends ConstInstruction {
    public static final String OPCODE_NAME = "aconst_null";

    public AconstNullInstruction(int offset) {
        super(Code.ACONST_NULL, 1, offset);
    }

    public Object getValue() {
        return null;
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
