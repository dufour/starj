package starj.coffer;

public class GetstaticInstruction extends FieldInstruction {
    public static final String OPCODE_NAME = "getstatic";

    public GetstaticInstruction(int offset, ConstantPool cp, int index) {
        super(Code.GETSTATIC, 3, offset, cp, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

