package starj.coffer;

public class GotoInstruction extends BranchInstruction {
    public static final String OPCODE_NAME = "goto";

    public GotoInstruction(int offset, short target) {
        super(Code.GOTO, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

