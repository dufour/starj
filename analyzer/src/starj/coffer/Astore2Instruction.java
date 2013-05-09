package starj.coffer;

public class Astore2Instruction extends StoreInstruction {
    public static final String OPCODE_NAME = "astore_2";

    public Astore2Instruction(int offset) {
        super(Code.ASTORE_2, 1, offset, ((short) 2));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
