package starj.coffer;

public class LstoreInstruction extends StoreInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lstore";

    public LstoreInstruction(int offset, short index) {
        super(Code.LSTORE, 2, offset, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
