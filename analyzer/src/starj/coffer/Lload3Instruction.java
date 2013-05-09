package starj.coffer;

public class Lload3Instruction extends LoadInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lload_3";

    public Lload3Instruction(int offset) {
        super(Code.LLOAD_3, 1, offset, ((short) 3));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
