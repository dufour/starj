package starj.toolkits.metrics;

import starj.EventBox;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;

public class BaseMetrics extends AbstractMetricOperation {
    public static final String CATEGORY = "base";

    public BaseMetrics(String name, String description) {
        super(name, description);
    }

    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();
        dep_set.add(new EventDependency(
                Event.CLASS_LOAD,
                new TotalMask(Constants.FIELD_RECORDED),
                true));
        dep_set.add(new EventDependency(
                Event.METHOD_ENTRY2,
                new TotalMask(Constants.FIELD_RECORDED),
                true,
                new EventDependency(
                    Event.METHOD_ENTRY,
                    new TotalMask(Constants.FIELD_RECORDED),
                    true)));
        dep_set.add(new EventDependency(
                Event.OBJECT_ALLOC,
                new TotalMask(Constants.FIELD_RECORDED),
                true));

        return dep_set;
    }

    public void apply(EventBox box, MetricRecord[] records) {
        Event event = box.getEvent();
        switch (event.getID()) {
            case Event.CLASS_LOAD:
                for (int i = 0; i < records.length; i++) {
                    ((BaseMetricsRecord) records[i]).class_load_count++;
                }
                break;
            case Event.METHOD_ENTRY:
            case Event.METHOD_ENTRY2:
                for (int i = 0; i < records.length; i++) {
                    ((BaseMetricsRecord) records[i]).method_entry_count++;
                }
                break;
            case Event.OBJECT_ALLOC: {
                    int obj_size = ((ObjectAllocEvent) event).getSize();
                    for (int i = 0; i < records.length; i++) {
                        BaseMetricsRecord r = (BaseMetricsRecord) records[i];
                        r.object_alloc_count++;
                        r.alloc_bytes += obj_size;
                    }
                }
                break;
            //case Event.INSTRUCTION_START:
            //    for (int i = 0; i < records.length; i++) {
            //        ((BaseMetricsRecord) records[i]).inst_count++;
            //    }
            //    break;
        }
    }

    public MetricRecord newRecord() {
        return new BaseMetricsRecord();
    }
    
    private class BaseMetricsRecord implements MetricRecord {
        public long class_load_count;
        public long method_entry_count;
        public long object_alloc_count;
        public long alloc_bytes;
    }

    public void accept(MetricVisitor visitor, MetricRecord record) {
        BaseMetricsRecord r = (BaseMetricsRecord) record;
        visitor.visit(new ValueMetric(CATEGORY, "classes",
                new LongMetricValue(r.class_load_count)));
        visitor.visit(new ValueMetric(CATEGORY, "methods",
                new LongMetricValue(r.method_entry_count)));
        visitor.visit(new ValueMetric(CATEGORY, "objects",
                new LongMetricValue(r.object_alloc_count)));
        long inst_count = InstructionCountManager.v().getInstructionCount(
                record);
        visitor.visit(new ValueMetric(CATEGORY, "instructions",
                new LongMetricValue(inst_count)));
        visitor.visit(new ValueMetric(CATEGORY, "bytes",
                new LongMetricValue(r.alloc_bytes)));
    }
}
