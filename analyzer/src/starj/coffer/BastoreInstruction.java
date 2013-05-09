package starj.coffer;

public class BastoreInstruction extends ArrayInstruction
        implements ByteInstruction {
    public static final String OPCODE_NAME = "bastore";
    
    public BastoreInstruction(int offset) {
        super(Code.BASTORE, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

