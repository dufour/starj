package starj.coffer;

public class AnewarrayInstruction extends ConstantPoolInstruction {
    public static final String OPCODE_NAME = "anewarray";

    public AnewarrayInstruction(int offset, ConstantPool cp, int index) {
        super(Code.ANEWARRAY, 3, offset, cp, index);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
