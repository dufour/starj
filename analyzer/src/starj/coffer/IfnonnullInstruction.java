package starj.coffer;

public class IfnonnullInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ifnonnull";

    public IfnonnullInstruction(int offset, int target) {
        super(Code.IFNONNULL, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

