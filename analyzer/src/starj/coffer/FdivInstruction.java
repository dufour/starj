package starj.coffer;

public class FdivInstruction extends Instruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fdiv";

    public FdivInstruction(int offset) {
        super(Code.FDIV, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

