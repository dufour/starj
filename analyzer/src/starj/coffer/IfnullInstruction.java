package starj.coffer;

public class IfnullInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ifnull";

    public IfnullInstruction(int offset, int target) {
        super(Code.IFNULL, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

