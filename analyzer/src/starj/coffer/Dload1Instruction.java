package starj.coffer;

public class Dload1Instruction extends LoadInstruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dload_1";

    public Dload1Instruction(int offset) {
        super(Code.DLOAD_1, 1, offset, ((short) 1));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
