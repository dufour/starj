package starj.coffer;

public class Iload1Instruction extends LoadInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "iload_1";

    public Iload1Instruction(int offset) {
        super(Code.ILOAD_1, 1, offset, ((short) 1));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
