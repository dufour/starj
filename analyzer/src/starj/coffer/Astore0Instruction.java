package starj.coffer;

public class Astore0Instruction extends StoreInstruction {
    public static final String OPCODE_NAME = "astore_0";

    public Astore0Instruction(int offset) {
        super(Code.ASTORE_0, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
