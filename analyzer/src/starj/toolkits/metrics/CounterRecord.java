package starj.toolkits.metrics;

public class CounterRecord implements MetricRecord {
    private long count;
    
    public CounterRecord() {
        this(0L);
    }

    public CounterRecord(long initial_count) {
        this.count = initial_count;
    }

    public void stepCount() {
        this.count += 1L;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getCount() {
        return this.count;
    }
}
