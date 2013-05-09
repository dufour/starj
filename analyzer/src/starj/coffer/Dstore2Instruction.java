package starj.coffer;

public class Dstore2Instruction extends StoreInstruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dstore_2";

    public Dstore2Instruction(int offset) {
        super(Code.DSTORE_2, 1, offset, ((short) 2));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
