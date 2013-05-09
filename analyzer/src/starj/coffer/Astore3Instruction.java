package starj.coffer;

public class Astore3Instruction extends StoreInstruction {
    public static final String OPCODE_NAME = "astore_3";

    public Astore3Instruction(int offset) {
        super(Code.ASTORE_3, 1, offset, ((short) 3));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
