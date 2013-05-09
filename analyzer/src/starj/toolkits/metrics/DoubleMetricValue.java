package starj.toolkits.metrics;

import java.text.DecimalFormat;

public class DoubleMetricValue implements MetricValue {
    public static final String TYPE = "double";
    
    private static DecimalFormat format;
    private double value;

    public DoubleMetricValue(double value) {
        this.value = value;
    }

    public DoubleMetricValue(long numerator, long denominator) {
        this.value = ((double) numerator) / denominator;
    }
    
    public String getType() {
        return DoubleMetricValue.TYPE;
    }

    public double getValue() {
        return this.value;
    }
    
    protected DecimalFormat getDecimalFormat() {
        if (DoubleMetricValue.format == null) {
            DoubleMetricValue.format = new DecimalFormat("0.000");
        }
        
        return DoubleMetricValue.format;
    }

    public String toString() {
        double v = this.getValue();
        if (Double.isNaN(v) || Double.isInfinite(v)) {
            return "N/A";
        }
        return this.getDecimalFormat().format(v);
    }
}
