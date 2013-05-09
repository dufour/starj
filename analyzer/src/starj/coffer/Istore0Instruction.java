package starj.coffer;

public class Istore0Instruction extends StoreInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "istore_0";

    public Istore0Instruction(int offset) {
        super(Code.ISTORE_0, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
