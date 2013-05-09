package starj.coffer;

public class FstoreInstruction extends StoreInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fstore";

    public FstoreInstruction(int offset, short index) {
        super(Code.FSTORE, 2, offset, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
