package starj.coffer;

public class DdivInstruction extends Instruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "ddiv";

    public DdivInstruction(int offset) {
        super(Code.DDIV, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

