package starj.coffer;

public class Dload3Instruction extends LoadInstruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dload_3";

    public Dload3Instruction(int offset) {
        super(Code.DLOAD_3, 1, offset, ((short) 3));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
