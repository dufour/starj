package starj.coffer;

public class ExceptionTableEntry {
    private int start_pc;
    private int end_pc;
    private int handler_pc;
    private int catch_type;

    public ExceptionTableEntry(int start_pc, int end_pc, int handler_pc,
            int catch_type) {
        this.start_pc = start_pc;
        this.end_pc = end_pc;
        this.handler_pc = handler_pc;
        this.catch_type = catch_type;
    }
    
    public int getStartPC() {
        return this.start_pc;
    }
    
    public int getEndPC() {
        return this.end_pc;
    }
    
    public int getHandlerPC() {
        return this.handler_pc;
    }
    
    public int getCatchType() {
        return this.catch_type;
    }
}
