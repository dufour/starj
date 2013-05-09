package starj.coffer;

public class Aload2Instruction extends LoadInstruction {
    public static final String OPCODE_NAME = "aload_2";

    public Aload2Instruction(int offset) {
        super(Code.ALOAD_2, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
