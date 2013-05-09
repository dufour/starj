package starj.toolkits.metrics;

import starj.Operation;

public interface MetricOperation extends Operation, MetricRecordFactory {
    public void accept(MetricVisitor visitor, MetricRecord record);
    public int getRecordsIndex();
}
