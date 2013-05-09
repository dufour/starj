package starj.coffer;

public class Lload2Instruction extends LoadInstruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "lload_2";

    public Lload2Instruction(int offset) {
        super(Code.LLOAD_2, 1, offset, ((short) 2));
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
