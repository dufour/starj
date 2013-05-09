package starj.coffer;

public class Dstore3Instruction extends StoreInstruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dstore_3";

    public Dstore3Instruction(int offset) {
        super(Code.DSTORE_3, 1, offset, ((short) 3));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
