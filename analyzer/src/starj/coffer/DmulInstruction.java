package starj.coffer;

public class DmulInstruction extends Instruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dmul";

    public DmulInstruction(int offset) {
        super(Code.DMUL, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

