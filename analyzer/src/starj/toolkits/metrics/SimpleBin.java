package starj.toolkits.metrics;

public class SimpleBin extends Bin {
    private BinKey key;
    
    public SimpleBin(BinKey key, MetricValue value) {
        super(value);
        this.key = key;
    }

    public BinKey getKey() {
        return this.key;
    }
    
    public String getKeyString() {
        return String.valueOf(this.key);
    }
}
