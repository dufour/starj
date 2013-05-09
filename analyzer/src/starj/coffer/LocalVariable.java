package starj.coffer;

public class LocalVariable {
    private int start_pc;
    private int length;
    private int name_index;
    private int descriptor_index;
    private int index;
    
    public LocalVariable(int start_pc, int length, int name_index,
            int descriptor_index, int index) {
        this.start_pc = start_pc;
        this.length = length;
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
        this.index = index;
    }
    
    public int getStartPC() {
        return this.start_pc;
    }
    
    public int getLength() {
        return this.length;
    }
    
    public int getNameIndex() {
        return this.name_index;
    }
    
    public int getDescriptorIndex() {
        return this.descriptor_index;
    }
    
    public int getIndex() {
        return this.index;
    }
}
