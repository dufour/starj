package starj.coffer;

public class ArraylengthInstruction extends ArrayInstruction {
    public static final String OPCODE_NAME = "arraylength";

    public ArraylengthInstruction(int offset) {
        super(Code.ARRAYLENGTH, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
