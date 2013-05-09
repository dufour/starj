package starj.coffer;

public class Ldc2WInstruction extends ConstantPoolInstruction {
    public static final String OPCODE_NAME = "ldc2_w";

    public Ldc2WInstruction(int offset, ConstantPool cp, int index) {
        super(Code.LDC2_W, 3, offset, cp, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

