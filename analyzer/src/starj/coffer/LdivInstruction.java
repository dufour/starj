package starj.coffer;

public class LdivInstruction extends Instruction implements
        LongInstruction {
    public static final String OPCODE_NAME = "ldiv";

    public LdivInstruction(int offset) {
        super(Code.LDIV, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

