package starj.coffer;

public class Lstore0Instruction extends StoreInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lstore_0";

    public Lstore0Instruction(int offset) {
        super(Code.LSTORE_0, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
