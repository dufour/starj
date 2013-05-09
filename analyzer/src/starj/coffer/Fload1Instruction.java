package starj.coffer;

public class Fload1Instruction extends LoadInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fload_1";

    public Fload1Instruction(int offset) {
        super(Code.FLOAD_1, 1, offset, ((short) 1));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
