package starj.coffer;

public class IconstM1Instruction extends ConstInstruction
        implements IntInstruction {
    public static final String OPCODE_NAME = "iconst_m1";

    public IconstM1Instruction(int offset) {
        super(Code.ICONST_M1, 1, offset);
    }

    public Object getValue() {
        return new Integer(-1);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

