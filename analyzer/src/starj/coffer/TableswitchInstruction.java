package starj.coffer;

public class TableswitchInstruction extends SwitchInstruction {
    public static final String OPCODE_NAME = "tableswitch";

    private int low;
    private int high;

    public TableswitchInstruction(int offset, byte pad, int default_index,
            int low, int high, int[] values, int[] offsets) {
        super(Code.TABLESWITCH, 
           // Code| Pad | D | L | H | Jump offsets
                1 + pad + 4 + 4 + 4 + (high - low + 1) * 4,
                offset, pad, default_index, values, offsets);
        this.low = low;
        this.high = high;
    }

    public int getLow() {
        return this.low;
    }

    public int getHigh() {
        return this.high;
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

