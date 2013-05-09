package starj.coffer;

public class I2lInstruction extends Instruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "i2l";

    public I2lInstruction(int offset) {
        super(Code.I2L, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

