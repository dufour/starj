package starj.coffer;

public class Aload0Instruction extends LoadInstruction {
    public static final String OPCODE_NAME = "aload_0";

    public Aload0Instruction(int offset) {
        super(Code.ALOAD_0, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
