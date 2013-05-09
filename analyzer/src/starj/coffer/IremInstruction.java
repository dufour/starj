package starj.coffer;

public class IremInstruction extends Instruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "irem";

    public IremInstruction(int offset) {
        super(Code.IREM, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

