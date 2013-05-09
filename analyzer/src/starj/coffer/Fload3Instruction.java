package starj.coffer;

public class Fload3Instruction extends LoadInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fload_3";

    public Fload3Instruction(int offset) {
        super(Code.FLOAD_3, 1, offset, ((short) 3));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
