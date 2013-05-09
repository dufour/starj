package starj.coffer;

public class I2bInstruction extends Instruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "i2b";

    public I2bInstruction(int offset) {
        super(Code.I2B, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

