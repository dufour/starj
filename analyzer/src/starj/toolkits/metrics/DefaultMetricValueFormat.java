package starj.toolkits.metrics;

import java.text.DecimalFormat;

public class DefaultMetricValueFormat extends AbstractMetricValueFormat {
    public static final String NOT_AVAILABLE = "N/A";
    private DecimalFormat percent_format;
    private DecimalFormat density_format;
    private DecimalFormat double_format;

    public DefaultMetricValueFormat() {
        this(null, null, null);
    }

    public DefaultMetricValueFormat(DecimalFormat format) {
        this(format, format, format);
    }

    public DefaultMetricValueFormat(DecimalFormat percent_format,
            DecimalFormat decimal_format) {
        this(percent_format, decimal_format, decimal_format);
    }

    public DefaultMetricValueFormat(DecimalFormat percent_format,
            DecimalFormat density_format, DecimalFormat double_format) {
        if (percent_format != null) {
            this.percent_format = percent_format;
        } else {
            this.percent_format = new DecimalFormat("0.000%");
        }
        if (density_format != null) {
            this.density_format = density_format;
        } else {
            this.density_format = new DecimalFormat("0.000");
        }
        if (double_format != null) {
            this.double_format = double_format;
        } else {
            this.double_format = new DecimalFormat("0.00000");
        }
    }

    public String formatPercentage(PercentageMetricValue value) {
        return this.formatDouble(value, this.percent_format);
    }
    
    public String formatDensity(DensityMetricValue value) {
        return this.formatDouble(value, this.density_format);
    }
    
    public String formatDouble(DoubleMetricValue value) {
        return this.formatDouble(value, this.double_format);
    }
    
    private String formatDouble(DoubleMetricValue value, DecimalFormat format) {
        double dbl_value = value.getValue();
        if (Double.isNaN(dbl_value) || Double.isInfinite(dbl_value)) {
            return DefaultMetricValueFormat.NOT_AVAILABLE;
        }

        return format.format(dbl_value);
    }
    
    public String formatInt(IntMetricValue value) {
        return String.valueOf(value.getValue());
    }
    
    public String formatLong(LongMetricValue value) {
        return String.valueOf(value.getValue());
    }
}
