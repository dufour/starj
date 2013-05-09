package starj.toolkits.metrics;

import starj.EventBox;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;

public class InstructionCountManager extends AbstractMetricOperation {
    private static InstructionCountManager instance
            = new InstructionCountManager();
    
    private InstructionCountManager() {
        super("counts", "Keeps track of executed bytecode counts");
    }

    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();
        dep_set.add(new EventDependency(
                Event.INSTRUCTION_START,
                new TotalMask(Constants.FIELD_RECORDED),
                true));

        return dep_set;
    }

    public void apply(EventBox box, MetricRecord[] records) {
        for (int i = 0; i < records.length; i++) {
            ((CounterRecord) records[i]).stepCount();
        }
        
    }

    public long getInstructionCount(MetricRecord record) {
        MetricRecord r = SampleSpaceManager.v().lookupMetricRecord(record,
                this);
        if (r != null) {
            return ((CounterRecord) r).getCount();
        }

        return -1L;
    }

    public MetricRecord newRecord() {
        return new CounterRecord();
    }

    public void accept(MetricVisitor visitor, MetricRecord record) {
        // Intentionally empty
    }
    
    public static InstructionCountManager v() {
        return InstructionCountManager.instance;
    }
}
