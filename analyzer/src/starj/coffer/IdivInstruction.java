package starj.coffer;

public class IdivInstruction extends Instruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "idiv";

    public IdivInstruction(int offset) {
        super(Code.IDIV, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

