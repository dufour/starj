package starj.coffer;

public class Iload3Instruction extends LoadInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "iload_3";

    public Iload3Instruction(int offset) {
        super(Code.ILOAD_3, 1, offset, ((short) 3));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
