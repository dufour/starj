package starj.coffer;

public class IfAcmpeqInstruction extends BranchInstruction {
    public static final String OPCODE_NAME = "if_acmpeq";

    public IfAcmpeqInstruction(int offset, int target) {
        super(Code.IF_ACMPEQ, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

