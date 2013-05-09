package starj.coffer;

public class Aload3Instruction extends LoadInstruction {
    public static final String OPCODE_NAME = "aload_3";

    public Aload3Instruction(int offset) {
        super(Code.ALOAD_3, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
