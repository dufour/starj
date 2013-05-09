package starj.coffer;

public class Iload0Instruction extends LoadInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "iload_0";

    public Iload0Instruction(int offset) {
        super(Code.ILOAD_0, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
