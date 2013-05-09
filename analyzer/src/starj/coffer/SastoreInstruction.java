package starj.coffer;

public class SastoreInstruction extends ArrayInstruction
        implements ShortInstruction {
    public static final String OPCODE_NAME = "sastore";

    public SastoreInstruction(int offset) {
        super(Code.SASTORE, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
