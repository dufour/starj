package starj.coffer;

public class Dstore0Instruction extends StoreInstruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dstore_0";

    public Dstore0Instruction(int offset) {
        super(Code.DSTORE_0, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
