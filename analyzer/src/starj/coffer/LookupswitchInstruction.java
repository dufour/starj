package starj.coffer;

public class LookupswitchInstruction extends SwitchInstruction {
    public static final String OPCODE_NAME = "lookupswitch";

    private int count;
    
    public LookupswitchInstruction(int offset, byte pad, int default_index,
            int count, int[] values, int[] offsets) {
        super(Code.LOOKUPSWITCH,
           // Code|Pad  | D | C | Jump offsets
                1 + pad + 4 + 4 + count * (4 + 4),
                offset, pad, default_index, values, offsets);
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

