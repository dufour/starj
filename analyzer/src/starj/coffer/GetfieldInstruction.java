package starj.coffer;

public class GetfieldInstruction extends FieldInstruction {
    public static final String OPCODE_NAME = "getfield";

    public GetfieldInstruction(int offset, ConstantPool cp, int index) {
        super(Code.GETFIELD, 3, offset, cp, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

