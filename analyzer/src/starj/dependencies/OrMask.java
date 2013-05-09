package starj.dependencies;

public class OrMask implements FieldMask {
    private FieldMask m1;
    private FieldMask m2;

    public OrMask(FieldMask m1, FieldMask m2) {
        this.m1 = m1;
        this.m2 = m2;
    }

    public boolean matches(int mask) {
        return this.m1.matches(mask) || this.m2.matches(mask);
    }
}
