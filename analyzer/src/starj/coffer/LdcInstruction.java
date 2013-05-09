package starj.coffer;

public class LdcInstruction extends ConstantPoolInstruction {
    public static final String OPCODE_NAME = "ldc";

    public LdcInstruction(int offset, ConstantPool cp, short index) {
        super(Code.LDC, 2, offset, cp, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

