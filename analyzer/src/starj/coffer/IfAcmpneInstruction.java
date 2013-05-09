package starj.coffer;

public class IfAcmpneInstruction extends BranchInstruction {
    public static final String OPCODE_NAME = "if_acmpne";

    public IfAcmpneInstruction(int offset, int target) {
        super(Code.IF_ACMPNE, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

