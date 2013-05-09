package starj.coffer;

public class Iload2Instruction extends LoadInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "iload_2";

    public Iload2Instruction(int offset) {
        super(Code.ILOAD_2, 1, offset, ((short) 2));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
