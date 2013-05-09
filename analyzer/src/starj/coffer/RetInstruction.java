package starj.coffer;

public class RetInstruction extends VariableInstruction {
    public static final String OPCODE_NAME = "ret";

    public RetInstruction(int offset, short index) {
        super(Code.RET, 2, offset, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

