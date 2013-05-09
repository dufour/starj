package starj.coffer;

public class SipushInstruction extends Instruction {
    public static final String OPCODE_NAME = "sipush";

    private short value;

    public SipushInstruction(int offset, short value) {
        super(Code.SIPUSH, 3, offset);
        this.value = value;
    }

    public short getShort() {
        return this.value;
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

