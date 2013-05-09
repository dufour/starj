package starj.coffer;

public class InvokespecialInstruction extends InvokeInstruction {
    public static final String OPCODE_NAME = "invokespecial";

    public InvokespecialInstruction(int offset, ConstantPool cp, int index) {
        super(Code.INVOKESPECIAL, 3, offset, cp, index);
    }

    public boolean isDynamic() {
        return false;
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

