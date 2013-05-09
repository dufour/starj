package starj.toolkits.metrics;

public interface MetricVisitor {
    public void init();
    public void enter(SampleSpace space);
    public void exit(SampleSpace space);
    public void enter(MetricSpace space);
    public void exit(MetricSpace space);
    public void visit(Metric metric);
    public void done();
}
