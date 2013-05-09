package starj.coffer;

public class Istore2Instruction extends StoreInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "istore_2";

    public Istore2Instruction(int offset) {
        super(Code.ISTORE_2, 1, offset, ((short) 2));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
