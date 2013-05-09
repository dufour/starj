package starj.coffer;

public class Dconst0Instruction extends ConstInstruction
        implements DoubleInstruction {
    public static final String OPCODE_NAME = "dconst_0";

    public Dconst0Instruction(int offset) {
        super(Code.DCONST_0, 1, offset);
    }

    public Object getValue() {
        return new Double(0.0);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

