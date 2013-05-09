package starj.coffer;

public class SwapInstruction extends Instruction {
    public static final String OPCODE_NAME = "swap";

    public SwapInstruction(int offset) {
        super(Code.SWAP, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

