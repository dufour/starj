package starj.coffer;

public abstract class SwitchInstruction extends Instruction {
    private byte pad;
    private int default_index;
    private int[] values;
    private int[] offsets;

    public SwitchInstruction(short opcode, int length, int offset, byte pad,
            int default_index, int[] values, int[] offsets) {
        super(opcode, length, offset);
        this.pad = pad;
        this.default_index = default_index;
        this.values = values;
        this.offsets = offsets;
    }
    
    public byte getPad() {
        return this.pad;
    }
    
    public int getDefaultIndex() {
        return this.default_index;
    }
    
    public int[] getValues() {
        return this.values;
    }
    
    public int getValue(int index) {
        return this.values[index];
    }
    
    public int[] getOffsets() {
        return this.offsets;
    }
    
    public int getOffset(int index) {
        return this.offsets[index];
    }
}

