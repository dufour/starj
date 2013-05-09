package starj.toolkits.metrics;

import java.text.DecimalFormat;

public class PercentageMetricValue extends DoubleMetricValue {
    public static final String TYPE = "percent";
    private static DecimalFormat format;
    
    public PercentageMetricValue(double value) {
        super(value);
    }
    
    public PercentageMetricValue(long numerator, long denominator) {
        super(numerator, denominator);
    }
    
    public String getType() {
        return PercentageMetricValue.TYPE;
    }
    
    protected DecimalFormat getDecimalFormat() {
        if (PercentageMetricValue.format == null) {
            PercentageMetricValue.format = new DecimalFormat("0.0%");
        }
        
        return PercentageMetricValue.format;
    }
}
