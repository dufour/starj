package starj.coffer;

public class Astore1Instruction extends StoreInstruction {
    public static final String OPCODE_NAME = "astore_1";

    public Astore1Instruction(int offset) {
        super(Code.ASTORE_1, 1, offset, ((short) 1));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
