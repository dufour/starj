package starj.coffer;

public class IfeqInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ifeq";

    public IfeqInstruction(int offset, int target) {
        super(Code.IFEQ, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

