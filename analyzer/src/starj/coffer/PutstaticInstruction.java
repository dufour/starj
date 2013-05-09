package starj.coffer;

public class PutstaticInstruction extends FieldInstruction {
    public static final String OPCODE_NAME = "putstatic";

    public PutstaticInstruction(int offset, ConstantPool cp, int index) {
        super(Code.PUTSTATIC, 3, offset, cp, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

