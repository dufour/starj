package starj.coffer;

public class IfgeInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ifge";

    public IfgeInstruction(int offset, int target) {
        super(Code.IFGE, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

