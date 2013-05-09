package starj.coffer;

public class FastoreInstruction extends ArrayInstruction
        implements FloatInstruction {
    public static final String OPCODE_NAME = "fastore";

    public FastoreInstruction(int offset) {
        super(Code.FASTORE, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
