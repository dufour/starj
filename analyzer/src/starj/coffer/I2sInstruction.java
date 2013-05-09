package starj.coffer;

public class I2sInstruction extends Instruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "i2s";

    public I2sInstruction(int offset) {
        super(Code.I2S, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

