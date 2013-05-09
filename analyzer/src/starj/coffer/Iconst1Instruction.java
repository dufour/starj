package starj.coffer;

public class Iconst1Instruction extends ConstInstruction
        implements IntInstruction {
    public static final String OPCODE_NAME = "iconst_1";

    public Iconst1Instruction(int offset) {
        super(Code.ICONST_1, 1, offset);
    }

    public Object getValue() {
        return new Integer(1);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

