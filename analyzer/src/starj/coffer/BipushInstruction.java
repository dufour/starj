package starj.coffer;

public class BipushInstruction extends Instruction {
    public static final String OPCODE_NAME = "bipush";
    
    private byte value;

    public BipushInstruction(int offset, byte value) {
        super(Code.BIPUSH, 2, offset);
        this.value = value;
    }

    public byte getByte() {
        return this.value;
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

