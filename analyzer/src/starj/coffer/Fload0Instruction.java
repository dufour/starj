package starj.coffer;

public class Fload0Instruction extends LoadInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fload_0";

    public Fload0Instruction(int offset) {
        super(Code.FLOAD_0, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
