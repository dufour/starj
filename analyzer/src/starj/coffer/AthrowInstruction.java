package starj.coffer;

public class AthrowInstruction extends Instruction {
    public static final String OPCODE_NAME = "athrow";

    public AthrowInstruction(int offset) {
        super(Code.ATHROW, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
