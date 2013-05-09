package starj.toolkits.metrics;

public abstract class Bin {
    private MetricValue value;
    
    public Bin(MetricValue value) {
        this.value = value;
    }
    
    public MetricValue getValue() {
        return this.value;
    }

    public abstract String getKeyString();

    public String toString() {
        return this.getKeyString() + ": " + this.value;
    }
}
