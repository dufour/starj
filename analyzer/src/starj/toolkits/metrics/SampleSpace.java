package starj.toolkits.metrics;

import starj.events.Event;

public abstract class SampleSpace {
    private String name;
    SampleSpaceManager owner; // Set in SampleSpaceManager
    private MetricSpace[] spaces;
    
    public SampleSpace(String name) {
        this.name = name;
        this.spaces = null;
    }
    
    public MetricSpace[] getMetricSpaces() {
        return (MetricSpace[]) this.spaces.clone();
    }

    void setOwner(SampleSpaceManager owner) {
        this.owner = owner;
        this.refreshMetricSpaces();
    }

    private void refreshMetricSpaces() {
        MetricSpace[] s = this.spaces;
        if (s != null) {
            for (int i = 0; i < s.length; i++) {
                s[i].setRecords(this.owner.newRecordSet());
            }
        }
    }

    public SampleSpaceManager getOwner() {
        return this.owner;
    }

    public abstract MetricSpace getMetricSpace(Event event);

    MetricSpace newMetricSpace(String name) {
        int count = (this.spaces != null ? this.spaces.length : 0);
        MetricSpace[] new_spaces = new MetricSpace[count + 1];
        if (count > 0) {
            System.arraycopy(this.spaces, 0, new_spaces, 0, count);
        }
        MetricRecord[] records = null;
        if (this.owner != null) {
            records = this.owner.newRecordSet();
        }
        MetricSpace rv = new MetricSpace(name, records);
        new_spaces[count] = rv;
        this.spaces = new_spaces;
        return rv;
    }

    void grow(MetricRecordFactory factory) {
        MetricSpace[] s = this.spaces;
        if (s != null) {
            for (int i = 0; i < s.length; i++) {
                s[i].add(factory.newRecord());
            }
        }
    }

    public String getName() {
        return this.name;
    }
}
