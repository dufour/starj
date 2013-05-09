package starj.coffer;

public class IfltInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "iflt";

    public IfltInstruction(int offset, int target) {
        super(Code.IFLT, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

