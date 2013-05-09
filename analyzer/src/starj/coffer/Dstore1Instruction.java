package starj.coffer;

public class Dstore1Instruction extends StoreInstruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dstore_1";

    public Dstore1Instruction(int offset) {
        super(Code.DSTORE_1, 1, offset, ((short) 1));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
