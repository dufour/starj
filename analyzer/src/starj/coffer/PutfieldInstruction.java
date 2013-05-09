package starj.coffer;

public class PutfieldInstruction extends FieldInstruction {
    public static final String OPCODE_NAME = "putfield";

    public PutfieldInstruction(int offset, ConstantPool cp, int index) {
        super(Code.PUTFIELD, 3, offset, cp, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

