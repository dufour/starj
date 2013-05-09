package starj.coffer;

public class I2cInstruction extends Instruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "i2c";

    public I2cInstruction(int offset) {
        super(Code.I2C, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

