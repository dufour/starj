package starj.coffer;

public class InvokevirtualInstruction extends InvokeInstruction {
    public static final String OPCODE_NAME = "invokevirtual";

    public InvokevirtualInstruction(int offset, ConstantPool cp, int index) {
        super(Code.INVOKEVIRTUAL, 3, offset, cp, index);
    }

    public boolean isDynamic() {
        return true;
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

