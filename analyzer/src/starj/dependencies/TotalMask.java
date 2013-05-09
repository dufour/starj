package starj.dependencies;

public class TotalMask implements FieldMask {
    private int mask;

    public TotalMask(int mask) {
        this.mask = mask;
    }
    
    public int getMask() {
        return this.mask;
    }

    public boolean matches(int mask) {
        return (this.mask & mask) == this.mask;
    }
}
