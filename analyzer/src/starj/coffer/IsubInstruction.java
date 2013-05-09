package starj.coffer;

public class IsubInstruction extends Instruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "isub";

    public IsubInstruction(int offset) {
        super(Code.ISUB, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

