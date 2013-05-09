package starj.coffer;

public class Lload0Instruction extends LoadInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lload_0";

    public Lload0Instruction(int offset) {
        super(Code.LLOAD_0, 1, offset, ((short) 0));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
