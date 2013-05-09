package starj.coffer;

public class InvokestaticInstruction extends InvokeInstruction {
    public static final String OPCODE_NAME = "invokestatic";

    public InvokestaticInstruction(int offset, ConstantPool cp, int index) {
        super(Code.INVOKESTATIC, 3, offset, cp, index);
    }

    public boolean isDynamic() {
        return false;
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

