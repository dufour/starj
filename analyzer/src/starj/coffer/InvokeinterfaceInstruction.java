package starj.coffer;

public class InvokeinterfaceInstruction extends InvokeInstruction {
    public static final String OPCODE_NAME = "invokeinterface";

    private int count;
    
    public InvokeinterfaceInstruction(int offset, ConstantPool cp, int index,
            short count) {
        super(Code.INVOKEINTERFACE, 5, offset, cp, index);
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public boolean isDynamic() {
        return true;
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

