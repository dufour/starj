package starj.dependencies;

public class NotMask implements FieldMask {
    private FieldMask mask;

    public NotMask(FieldMask mask) {
        this.mask = mask;
    }

    public boolean matches(int mask) {
        return !this.mask.matches(mask);
    }
}
