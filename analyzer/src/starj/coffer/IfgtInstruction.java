package starj.coffer;

public class IfgtInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "ifgt";

    public IfgtInstruction(int offset, int target) {
        super(Code.IFGT, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

