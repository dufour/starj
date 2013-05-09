package starj.coffer;

public class JsrWInstruction extends BranchInstruction {
    public static final String OPCODE_NAME = "jsr_w";

    public JsrWInstruction(int offset, int target) {
        super(Code.JSR_W, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

