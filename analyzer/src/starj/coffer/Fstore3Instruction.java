package starj.coffer;

public class Fstore3Instruction extends StoreInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fstore_3";

    public Fstore3Instruction(int offset) {
        super(Code.FSTORE_3, 1, offset, ((short) 3));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
