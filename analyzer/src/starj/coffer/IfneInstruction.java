package starj.coffer;

public class IfneInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ifne";

    public IfneInstruction(int offset, int target) {
        super(Code.IFNE, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

