package starj.coffer;

public class GotoWInstruction extends BranchInstruction {
    public static final String OPCODE_NAME = "goto_w";

    public GotoWInstruction(int offset, int target) {
        super(Code.GOTO_W, 5, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

