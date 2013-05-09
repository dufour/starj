package starj.toolkits.metrics;

public class RangeBin extends Bin {
    private BinKey low;
    private BinKey high;
    
    public RangeBin(BinKey low_key, BinKey high_key, MetricValue value) {
        super(value);
        this.low = low_key;
        this.high = high_key;
    }

    public BinKey getLowKey() {
        return this.low;
    }

    public BinKey getHighKey() {
        return this.high;
    }
    
    public String getKeyString() {
        return this.low + "-" + this.high;
    }
}
