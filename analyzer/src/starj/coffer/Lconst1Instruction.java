package starj.coffer;

public class Lconst1Instruction extends ConstInstruction
        implements LongInstruction {
    public static final String OPCODE_NAME = "lconst_1";

    public Lconst1Instruction(int offset) {
        super(Code.LCONST_1, 1, offset);
    }

    public Object getValue() {
        return new Long(1);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

