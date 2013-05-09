package starj.toolkits.metrics;

public abstract class AbstractMetricVisitor implements MetricVisitor {
    public void init() {
        // Intentionally empty
    }
    
    public abstract void enter(SampleSpace space);
    public abstract void exit(SampleSpace space);
    public abstract void enter(MetricSpace space);
    public abstract void exit(MetricSpace space);
    
    public void visit(Metric metric) {
        if (metric instanceof ValueMetric) {
            this.visitValueMetric((ValueMetric) metric);
        } else if (metric instanceof PercentileMetric) {
            this.visitPercentileMetric((PercentileMetric) metric);
        } else if (metric instanceof BinMetric) {
            this.visitBinMetric((BinMetric) metric);
        }
    }

    public abstract void visitValueMetric(ValueMetric metric);
    public abstract void visitPercentileMetric(PercentileMetric metric);
    public abstract void visitBinMetric(BinMetric metric);

    public void done() {
        // Intentionally empty
    }
}
