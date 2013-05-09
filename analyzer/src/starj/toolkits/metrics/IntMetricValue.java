package starj.toolkits.metrics;

public class IntMetricValue implements MetricValue {
    public static final String TYPE = "int";
    
    private int value;

    public IntMetricValue(int value) {
        this.value = value;
    }

    public String getType() {
        return IntMetricValue.TYPE;
    }
    
    public int getValue() {
        return this.value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
