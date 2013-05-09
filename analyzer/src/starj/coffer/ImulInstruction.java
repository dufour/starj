package starj.coffer;

public class ImulInstruction extends Instruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "imul";

    public ImulInstruction(int offset) {
        super(Code.IMUL, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

