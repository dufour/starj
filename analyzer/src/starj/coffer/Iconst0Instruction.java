package starj.coffer;

public class Iconst0Instruction extends ConstInstruction
        implements IntInstruction {
    public static final String OPCODE_NAME = "iconst_0";

    public Iconst0Instruction(int offset) {
        super(Code.ICONST_0, 1, offset);
    }

    public Object getValue() {
        return new Integer(0);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

