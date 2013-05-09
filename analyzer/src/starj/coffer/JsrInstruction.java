package starj.coffer;

public class JsrInstruction extends BranchInstruction {
    public static final String OPCODE_NAME = "jsr";

    public JsrInstruction(int offset, short target) {
        super(Code.JSR, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

