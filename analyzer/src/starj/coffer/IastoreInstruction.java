package starj.coffer;

public class IastoreInstruction extends ArrayInstruction
        implements IntInstruction {
    public static final String OPCODE_NAME = "iastore";

    public IastoreInstruction(int offset) {
        super(Code.IASTORE, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
