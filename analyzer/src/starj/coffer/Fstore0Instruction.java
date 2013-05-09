package starj.coffer;

public class Fstore0Instruction extends StoreInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fstore_0";

    public Fstore0Instruction(int offset) {
        super(Code.FSTORE_0, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
