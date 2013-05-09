package starj.coffer;

public class IincInstruction extends VariableInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "iinc";

    private byte amount;

    public IincInstruction(int offset, short index, byte amount) {
        super(Code.IINC, 3, offset, amount);
        this.amount = amount;
    }

    public byte getConst() {
        return this.amount;
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

