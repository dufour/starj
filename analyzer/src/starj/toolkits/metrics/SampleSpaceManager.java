package starj.toolkits.metrics;

import starj.events.Event;

public class SampleSpaceManager {
    private static SampleSpaceManager instance = null;
    private MetricOperation[] operations;
    private SampleSpace[] spaces;
    private MetricSpace[] metric_spaces; // Cache

    private SampleSpaceManager() {
        // no instances
        this.register(new WholeSampleSpace());
        this.register(new StaticApplicationSampleSpace());
    }

    public int register(MetricOperation operation) {
        // Grow metric operation array
        int count = (this.operations != null ? this.operations.length : 0);
        MetricOperation[] new_operations = new MetricOperation[count + 1];
        if (count > 0) {
            System.arraycopy(this.operations, 0, new_operations, 0, count);
        }
        new_operations[count] = operation;
        this.operations = new_operations;

        // Create storage in metric records
        SampleSpace[] s = this.spaces;
        if (s != null) {
            for (int i = 0; i < s.length; i++) {
                s[i].grow(operation);
            }
        }

        // Return new storage index
        return count;
    }

    public void register(SampleSpace space) {
        // Grow array
        int count = (this.spaces != null ? this.spaces.length : 0);
        SampleSpace[] new_spaces = new SampleSpace[count + 1];
        if (count > 0) {
            System.arraycopy(this.spaces, 0, new_spaces, 0, count);
        }
        new_spaces[count] = space;
        this.spaces = new_spaces;

        // Register this manager as owner of the sample space
        space.setOwner(this);
    }

    MetricRecord[] newRecordSet() {
        int count = (this.operations != null ? this.operations.length : 0);
        MetricRecord[] rv = new MetricRecord[count];
        for (int i = 0; i < count; i++) {
            rv[i] = this.operations[i].newRecord();
        }

        return rv;
    }

    public MetricSpace[] getMetricSpaces(Event event) {
        MetricSpace[] rv = this.metric_spaces;
        int count = (this.spaces != null ? this.spaces.length : 0);
        if (rv == null || rv.length != count) {
            rv = new MetricSpace[count];
            this.metric_spaces = rv; // Cache this array
        }
        for (int i = 0; i < count; i++) {
            rv[i] = this.spaces[i].getMetricSpace(event);
        }

        return rv;
    }
    
    public SampleSpace[] getSampleSpaces() {
        return (SampleSpace[]) this.spaces.clone();
    }
    
    /**
     * Returns the <code>MetricRecord</code> from <code>destination_op</code>
     * which belongs to the same <code>MetricSpace</code> as
     * <code>source_record</code>.
     * 
     * @param source_record The <code>MetricRecord</code> instance to lookup.
     * @param destination_op The <code>MetricOperation</code> instance to
     *   be mapped to.
     * @return The <code>MetricRecord</code> instance associated with
     * <code>destination_op</code> which belongs to the same
     * <code>MetricSpace</code> as <code>source_record</code>, or
     * <code>null</code> on error.
     */
    public MetricRecord lookupMetricRecord(MetricRecord source_record,
            MetricOperation destination_op) {
        /* This is a brain dead implementation, which preforms a full lookup
         * on every query. A much more efficient approach would involve
         * providing a mapping from a MetricRecord instance back to its
         * MetricSpace. This may require that MetricRecord gets moved to
         * a class instead of an interface */
        
        if (this.spaces == null) {
            return null;
        }
        
        for (int i = 0; i < this.spaces.length; i++) {
            SampleSpace sample_space = this.spaces[i];
            MetricSpace[] metric_spaces = sample_space.getMetricSpaces();
            if (metric_spaces != null) {
                for (int j = 0; j < metric_spaces.length; j++) {
                    MetricSpace metric_space = metric_spaces[j];
                    MetricRecord[] records = metric_space.getMetricRecords();
                    if (records != null) {
                        for (int k = 0; k < records.length; k++) {
                            MetricRecord r = records[k];
                            if (r == source_record) {
                                // Found a match!
                                return metric_space.getRecord(
                                        destination_op.getRecordsIndex());
                            }
                        }
                    }
                }
            }
        }
        
        return null;
    }

    public static SampleSpaceManager v() {
        if (instance == null) {
            instance = new SampleSpaceManager();
        }
        return instance;
    }
}
