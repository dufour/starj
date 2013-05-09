package starj.coffer;

public class IaddInstruction extends Instruction
        implements IntInstruction {
    public static final String OPCODE_NAME = "iadd";

    public IaddInstruction(int offset) {
        super(Code.IADD, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

