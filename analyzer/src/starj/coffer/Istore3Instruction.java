package starj.coffer;

public class Istore3Instruction extends StoreInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "istore_3";

    public Istore3Instruction(int offset) {
        super(Code.ISTORE_3, 1, offset, ((short) 3));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
