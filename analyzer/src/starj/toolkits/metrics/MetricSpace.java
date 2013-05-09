package starj.toolkits.metrics;

public class MetricSpace {
    private String name;
    private MetricRecord[] records;
    
    public MetricSpace(String name) {
        this(name, null);
    }

    public MetricSpace(String name, MetricRecord[] records) {
        this.name = name;
        this.records = records;
    }
    
    public MetricRecord[] getMetricRecords() {
        return (MetricRecord[]) this.records.clone();
    }

    void setRecords(MetricRecord[] records) {
        this.records = records;
    }

    void add(MetricRecord record) {
        int count = (this.records != null ? this.records.length : 0);
        MetricRecord[] new_records = new MetricRecord[count + 1];
        if (count > 0) {
            System.arraycopy(this.records, 0, new_records, 0, count);
        }
        new_records[count] = record;
        this.setRecords(new_records);
    }

    public MetricRecord getRecord(int index) {
        return this.records[index];
    }

    public String getName() {
        return this.name;
    }
}
