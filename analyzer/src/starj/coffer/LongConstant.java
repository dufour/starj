package starj.coffer;

public class LongConstant extends Constant {
    private long value;
    
    public LongConstant(long value) {
        super(Constant.CONSTANT_Long);
        this.value = value;
    }

    public long getValue() {
        return this.value;
    }
}
