package starj.coffer;

public class Lload1Instruction extends LoadInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lload_1";

    public Lload1Instruction(int offset) {
        super(Code.LLOAD_1, 1, offset, ((short) 1));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
