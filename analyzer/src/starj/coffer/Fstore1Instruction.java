package starj.coffer;

public class Fstore1Instruction extends StoreInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fstore_1";

    public Fstore1Instruction(int offset) {
        super(Code.FSTORE_1, 1, offset, ((short) 1));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
