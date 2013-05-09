package starj.coffer;

public class InstanceofInstruction extends ConstantPoolInstruction {
    public static final String OPCODE_NAME = "instanceof";

    public InstanceofInstruction(int offset, ConstantPool cp, int index) {
        super(Code.INSTANCEOF, 3, offset, cp, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

