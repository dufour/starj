package starj.coffer;

public class Lstore1Instruction extends StoreInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lstore_1";

    public Lstore1Instruction(int offset) {
        super(Code.LSTORE_1, 1, offset, ((short) 1));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
