package starj.coffer;

public class Iconst4Instruction extends ConstInstruction
        implements IntInstruction {
    public static final String OPCODE_NAME = "iconst_4";

    public Iconst4Instruction(int offset) {
        super(Code.ICONST_4, 1, offset);
    }

    public Object getValue() {
        return new Integer(4);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

