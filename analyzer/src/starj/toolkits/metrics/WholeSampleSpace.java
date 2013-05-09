package starj.toolkits.metrics;

import starj.events.Event;

public class WholeSampleSpace extends SampleSpace {
    private MetricSpace space;

    public WholeSampleSpace() {
        super("whole");
        this.space = this.newMetricSpace("whole");
    }


    public MetricSpace getMetricSpace(Event event) {
        return this.space;
    }
}
