package starj.coffer;

public class Dconst1Instruction extends ConstInstruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dconst_1";

    public Dconst1Instruction(int offset) {
        super(Code.DCONST_1, 1, offset);
    }

    public Object getValue() {
        return new Double(1.0);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

