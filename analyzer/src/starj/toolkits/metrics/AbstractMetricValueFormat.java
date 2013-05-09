package starj.toolkits.metrics;

public abstract class AbstractMetricValueFormat implements MetricValueFormat {
    public String format(MetricValue value) {
        if (value instanceof PercentageMetricValue) {
            return this.formatPercentage((PercentageMetricValue) value);
        } else if (value instanceof DensityMetricValue) {
            return this.formatDensity((DensityMetricValue) value);
        } else if (value instanceof DoubleMetricValue) {
            return this.formatDouble((DoubleMetricValue) value);
        } else if (value instanceof IntMetricValue) {
            return this.formatInt((IntMetricValue) value);
        } else if (value instanceof LongMetricValue) {
            return this.formatLong((LongMetricValue) value);
        }

        throw new RuntimeException("Unsupported metric value class: '"
                + value.getClass().getName() + "'");
    }
    
    public abstract String formatPercentage(PercentageMetricValue value);
    public abstract String formatDensity(DensityMetricValue value);
    public abstract String formatDouble(DoubleMetricValue value);
    public abstract String formatInt(IntMetricValue value);
    public abstract String formatLong(LongMetricValue value);
}
