package starj.toolkits.metrics;

import java.io.PrintStream;
import java.text.DecimalFormat;

import starj.util.StringUtils;

public class TextMetricVisitor extends AbstractMetricVisitor {
    private static final int INDENT_STEP = 4;
    private static String VALUE_SEP = ": ";
    private static DecimalFormat threshold_format = new DecimalFormat("0.0%");

    private PrintStream out;
    private MetricValueFormat format;
    private int indent_level;

    public TextMetricVisitor() {
        this(System.out);
    }

    public TextMetricVisitor(MetricValueFormat format) {
        this(System.out, format);
    }
    
    public TextMetricVisitor(PrintStream out) {
        this(out, new DefaultMetricValueFormat());
    }

    public TextMetricVisitor(PrintStream out, MetricValueFormat format) {
        this.out = out;
        this.format = format;
        this.indent_level = 0;
    }
    
    private void display(String text) {
        this.out.println(StringUtils.indent(text, this.indent_level));
    }
    
    public void visitValueMetric(ValueMetric metric) {
        this.display(metric.getFullName() + VALUE_SEP
                + this.format.format(metric.getValue()));
    }
    
    public void enter(SampleSpace space) {
        this.display("Sample space: " + space.getName());
        this.indent_level += TextMetricVisitor.INDENT_STEP;
    }
    
    public void exit(SampleSpace space) {
        this.indent_level -= TextMetricVisitor.INDENT_STEP;
    }
    
    public void enter(MetricSpace space) {
        this.display("Metric space: " + space.getName());
        this.indent_level += TextMetricVisitor.INDENT_STEP;
    }
    
    public void exit(MetricSpace space) {
        this.indent_level -= TextMetricVisitor.INDENT_STEP;
    }
    
    public void visitPercentileMetric(PercentileMetric metric) {
        this.display(metric.getFullName()
                + " (" + threshold_format.format(metric.getThreshold()) + ")"
                + VALUE_SEP
                + this.format.format(metric.getValue()));
    }
    
    public void visitBinMetric(BinMetric metric) {
        String name = metric.getFullName();
        Bin[] bins = metric.getBins();
        for (int i = 0; i < bins.length; i++) {
            Bin bin = bins[i];
            this.display(name + "[" + bin.getKeyString() + "]"
                    + TextMetricVisitor.VALUE_SEP
                    + this.format.format(bin.getValue()));
        }
    }
}
