package starj.coffer;

public class Aload1Instruction extends LoadInstruction {
    public static final String OPCODE_NAME = "aload_1";

    public Aload1Instruction(int offset) {
        super(Code.ALOAD_1, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
