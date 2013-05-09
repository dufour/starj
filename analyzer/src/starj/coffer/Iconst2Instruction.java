package starj.coffer;

public class Iconst2Instruction extends ConstInstruction
        implements IntInstruction {
    public static final String OPCODE_NAME = "iconst_2";

    public Iconst2Instruction(int offset) {
        super(Code.ICONST_2, 1, offset);
    }

    public Object getValue() {
        return new Integer(2);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

