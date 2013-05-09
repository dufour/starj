package starj.coffer;

public class Fstore2Instruction extends StoreInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fstore_2";

    public Fstore2Instruction(int offset) {
        super(Code.FSTORE_2, 1, offset, ((short) 2));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
