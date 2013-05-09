package starj.coffer;

public class Lconst0Instruction extends ConstInstruction
        implements LongInstruction {
    public static final String OPCODE_NAME = "lconst_0";

    public Lconst0Instruction(int offset) {
        super(Code.LCONST_0, 1, offset);
    }

    public Object getValue() {
        return new Long(0);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

