package starj.coffer;

public class NewInstruction extends ConstantPoolInstruction {
    public static final String OPCODE_NAME = "new";

    public NewInstruction(int offset, ConstantPool cp, int index) {
        super(Code.NEW, 3, offset, cp, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

