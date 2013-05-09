package starj.coffer;

public class BaloadInstruction extends ArrayInstruction
        implements ByteInstruction {
    public static final String OPCODE_NAME = "baload";
    
    public BaloadInstruction(int offset) {
        super(Code.BALOAD, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

