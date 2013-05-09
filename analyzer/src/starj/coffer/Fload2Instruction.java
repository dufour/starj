package starj.coffer;

public class Fload2Instruction extends LoadInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fload_2";

    public Fload2Instruction(int offset) {
        super(Code.FLOAD_2, 1, offset, ((short) 2));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
