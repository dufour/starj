package starj.toolkits.metrics;

public class DensityMetricValue extends DoubleMetricValue {
    public static final String TYPE = "density";
    
    public DensityMetricValue(double value) {
        super(value);
    }

    public DensityMetricValue(long numerator, long denominator) {
        super(((double) numerator) / denominator * 1000.0);
    }
    
    public String getType() {
        return DensityMetricValue.TYPE;
    }
}
