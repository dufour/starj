package starj.coffer;

public class Lstore2Instruction extends StoreInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lstore_2";

    public Lstore2Instruction(int offset) {
        super(Code.LSTORE_2, 1, offset, ((short) 2));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
