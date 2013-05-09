package starj.coffer;

public class DstoreInstruction extends StoreInstruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dstore";

    public DstoreInstruction(int offset, short index) {
        super(Code.DSTORE, 2, offset, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
