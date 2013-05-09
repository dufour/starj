package starj.coffer;

public class Fconst0Instruction extends ConstInstruction
        implements FloatInstruction {
    public static final String OPCODE_NAME = "fconst_0";

    public Fconst0Instruction(int offset) {
        super(Code.FCONST_0, 1, offset);
    }

    public Object getValue() {
        return new Float(0.0f);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

