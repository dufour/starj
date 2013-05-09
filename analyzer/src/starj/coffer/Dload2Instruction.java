package starj.coffer;

public class Dload2Instruction extends LoadInstruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dload_2";

    public Dload2Instruction(int offset) {
        super(Code.DLOAD_2, 1, offset, ((short) 2));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
