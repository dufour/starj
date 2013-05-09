package starj.coffer;

public class Iconst5Instruction extends ConstInstruction
        implements IntInstruction {
    public static final String OPCODE_NAME = "iconst_5";

    public Iconst5Instruction(int offset) {
        super(Code.ICONST_5, 1, offset);
    }

    public Object getValue() {
        return new Integer(5);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

