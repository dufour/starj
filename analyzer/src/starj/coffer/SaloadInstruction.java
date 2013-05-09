package starj.coffer;

public class SaloadInstruction extends ArrayInstruction
        implements ShortInstruction {
    public static final String OPCODE_NAME = "saload";

    public SaloadInstruction(int offset) {
        super(Code.SALOAD, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
