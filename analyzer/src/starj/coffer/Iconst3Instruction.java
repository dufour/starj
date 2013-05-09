package starj.coffer;

public class Iconst3Instruction extends ConstInstruction
        implements IntInstruction {
    public static final String OPCODE_NAME = "iconst_3";

    public Iconst3Instruction(int offset) {
        super(Code.ICONST_3, 1, offset);
    }

    public Object getValue() {
        return new Integer(3);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

