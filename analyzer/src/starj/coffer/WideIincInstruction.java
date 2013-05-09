package starj.coffer;

public class WideIincInstruction extends WideInstruction implements
        IntInstruction {
    private short amount;

    public WideIincInstruction(int offset, int index, short amount) {
        super(offset, Code.IINC, index);
        this.amount = amount;
    }

    public short getConst() {
        return this.amount;
    }
}

