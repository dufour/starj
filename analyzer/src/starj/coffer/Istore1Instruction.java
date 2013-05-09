package starj.coffer;

public class Istore1Instruction extends StoreInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "istore_1";

    public Istore1Instruction(int offset) {
        super(Code.ISTORE_1, 1, offset, ((short) 1));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
