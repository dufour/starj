package starj.dependencies;

public class PartialMask implements FieldMask {
    private int mask;

    public PartialMask(int mask) {
        this.mask = mask;
    }
    
    public int getMask() {
        return this.mask;
    }

    public boolean matches(int mask) {
        return (this.mask & mask) != 0;
    }
}
