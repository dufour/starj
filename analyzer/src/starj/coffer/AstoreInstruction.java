package starj.coffer;

public class AstoreInstruction extends StoreInstruction {
    public static final String OPCODE_NAME = "astore";

    public AstoreInstruction(int offset, short index) {
        super(Code.ASTORE, 2, offset, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
