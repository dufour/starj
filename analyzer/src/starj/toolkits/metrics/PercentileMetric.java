package starj.toolkits.metrics;

public class PercentileMetric extends Metric {
    private MetricValue value;
    private float threshold;

    public PercentileMetric(String category, String name, MetricValue value,
            float threshold) {
        super(category, name);
        this.value = value;
        this.threshold = threshold;
    }

    public String getKind() {
        return "percentile";
    }
    
    public MetricValue getValue() {
        return this.value;
    }
    
    public float getThreshold() {
        return this.threshold;
    }
}
