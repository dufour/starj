package starj.coffer;

public class IfleInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ifle";

    public IfleInstruction(int offset, int target) {
        super(Code.IFLE, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

