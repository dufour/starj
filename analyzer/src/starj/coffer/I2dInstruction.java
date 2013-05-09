package starj.coffer;

public class I2dInstruction extends Instruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "i2d";

    public I2dInstruction(int offset) {
        super(Code.I2D, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

