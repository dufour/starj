package starj.coffer;

public class I2fInstruction extends Instruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "i2f";

    public I2fInstruction(int offset) {
        super(Code.I2F, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

