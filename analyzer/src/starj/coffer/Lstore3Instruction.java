package starj.coffer;

public class Lstore3Instruction extends StoreInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lstore_3";

    public Lstore3Instruction(int offset) {
        super(Code.LSTORE_3, 1, offset, ((short) 3));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
