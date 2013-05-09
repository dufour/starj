package starj.coffer;

public class Fconst2Instruction extends ConstInstruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fconst_2";

    public Fconst2Instruction(int offset) {
        super(Code.FCONST_2, 1, offset);
    }

    public Object getValue() {
        return new Float(2.0f);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

