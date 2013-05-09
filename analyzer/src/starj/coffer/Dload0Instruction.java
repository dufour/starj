package starj.coffer;

public class Dload0Instruction extends LoadInstruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dload_0";

    public Dload0Instruction(int offset) {
        super(Code.DLOAD_0, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
