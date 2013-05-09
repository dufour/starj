package starj.toolkits.metrics;

public class ValueMetric extends Metric {
    private MetricValue value;

    public ValueMetric(String category, String name, MetricValue value) {
        super(category, name);
        this.value = value;
    }

    public MetricValue getValue() {
        return this.value;
    }

    public String getKind() {
        return "value";
    }
}
