package starj.coffer;

public class AastoreInstruction extends ArrayInstruction {
    public static final String OPCODE_NAME = "aastore";

    public AastoreInstruction(int offset) {
        super(Code.AASTORE, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
