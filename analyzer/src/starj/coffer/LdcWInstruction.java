package starj.coffer;

public class LdcWInstruction extends ConstantPoolInstruction {
    public static final String OPCODE_NAME = "ldc_w";

    public LdcWInstruction(int offset, ConstantPool cp, int index) {
        super(Code.LDC_W, 3, offset, cp, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

