package starj.toolkits.metrics;

import starj.AbstractOperation;
import starj.EventBox;
import starj.events.Event;

public abstract class AbstractMetricOperation extends AbstractOperation
    implements MetricOperation {
    private int records_index;
    private MetricRecord[] records; // Cached record array (avoids array allocs)
    
    public AbstractMetricOperation(String name, String description) {
        super(name, description);
        this.records_index = SampleSpaceManager.v().register(this);
    }

    public int getRecordsIndex() {
        return this.records_index;
    }

    public final void apply(EventBox box) {
        this.apply(box, this.getMetricRecords(box.getEvent()));
    }

    protected MetricRecord[] getMetricRecords(Event event) {
        MetricSpace[] spaces = SampleSpaceManager.v().getMetricSpaces(event);
        MetricRecord[] records = this.records;
        if (records == null || records.length != spaces.length) {
            records = new MetricRecord[spaces.length];
            this.records = records;
        }

        int records_index = this.records_index;
        for (int i = 0; i < spaces.length; i++) {
            records[i] = spaces[i].getRecord(records_index);
        }

        return records;
    }

    public abstract void apply(EventBox box, MetricRecord[] records);
}
