package starj.coffer;

public class IstoreInstruction extends StoreInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "istore";

    public IstoreInstruction(int offset, short index) {
        super(Code.ISTORE, 2, offset, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
