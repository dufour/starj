package starj.toolkits.metrics;

public class LongMetricValue implements MetricValue {
    public static final String TYPE = "long";
    private long value;

    public LongMetricValue(long value) {
        this.value = value;
    }

    public String getType() {
        return LongMetricValue.TYPE;
    }
    
    public long getValue() {
        return this.value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
