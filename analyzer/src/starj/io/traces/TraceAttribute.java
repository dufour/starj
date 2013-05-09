package starj.io.traces;

public abstract class TraceAttribute {
    public String name;
    public int length;
    
    public TraceAttribute(String name, int length) {
        this.name = name;
        this.length = length;
    }
    
    public String getName() {
        return this.name;
    }

    public int getLength() {
        return this.length;
    }
}
