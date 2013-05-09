package starj.coffer;

public class AaloadInstruction extends ArrayInstruction {
    public static final String OPCODE_NAME = "aaload";
    
    public AaloadInstruction(int offset) {
        super(Code.AALOAD, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
