package starj.coffer;

public class LineNumber {
    private int start_pc;
    private int line_number;
    
    public LineNumber(int start_pc, int line_number) {
        this.start_pc = start_pc;
        this.line_number = line_number;
    }

    public int getStartPC() {
        return this.start_pc;
    }

    public int getLineNumber() {
        return this.line_number;
    }
}
