package starj.coffer;

public class Fconst1Instruction extends ConstInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fconst_1";

    public Fconst1Instruction(int offset) {
        super(Code.FCONST_1, 1, offset);
    }

    public Object getValue() {
        return new Float(1.0f);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

