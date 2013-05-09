package starj.toolkits.metrics;

import starj.EventBox;
import starj.coffer.*;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;
import starj.toolkits.services.InstructionResolver;

public class ArrayMetrics extends AbstractMetricOperation {
    public static final String CATEGORY = "data";

    public ArrayMetrics(String name, String description) {
        super(name, description);
    }

    public OperationSet operationDependencies() {
        OperationSet dep_set = super.operationDependencies();
        dep_set.add(InstructionResolver.v());

        return dep_set;
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
        InstructionStartEvent e = (InstructionStartEvent) box.getEvent();
        short opcode = e.getOpcode();

        switch (opcode) {
            case Code.AALOAD:
            case Code.AASTORE:
                for (int i = 0; i < records.length; i++) {
                    ArrayMetricsRecord r = (ArrayMetricsRecord) records[i];
                    r.ref_array_accesses++;
                    r.total_array_accesses++;
                }
                break;
            case Code.CALOAD:
            case Code.CASTORE:
                for (int i = 0; i < records.length; i++) {
                    ArrayMetricsRecord r = (ArrayMetricsRecord) records[i];
                    r.char_array_accesses++;
                    r.total_array_accesses++;
                }
                break;
            case Code.BALOAD:
            case Code.BASTORE:
            case Code.DALOAD:
            case Code.DASTORE:
            case Code.FALOAD:
            case Code.FASTORE:
            case Code.IALOAD:
            case Code.IASTORE:
            case Code.LALOAD:
            case Code.LASTORE:
            case Code.SALOAD:
            case Code.SASTORE:
                for (int i = 0; i < records.length; i++) {
                    ArrayMetricsRecord r = (ArrayMetricsRecord) records[i];
                    r.num_array_accesses++;
                    r.total_array_accesses++;
                }
                break;
            case Code.ARRAYLENGTH:
            case Code.NEWARRAY:
            case Code.MULTIANEWARRAY:
            case Code.ANEWARRAY:
                for (int i = 0; i < records.length; i++) {
                    ArrayMetricsRecord r = (ArrayMetricsRecord) records[i];
                    r.total_array_accesses++;
                }
                break;
            default:
                /* Not an array instruction */
                return;
        }
    }

    public void accept(MetricVisitor visitor, MetricRecord record) {
        ArrayMetricsRecord r = (ArrayMetricsRecord) record;
        long inst_count = InstructionCountManager.v().getInstructionCount(
                record);
        visitor.visit(new ValueMetric(CATEGORY, "arrayDensity",
                new DensityMetricValue(r.total_array_accesses,
                        inst_count)));
        visitor.visit(new ValueMetric(CATEGORY, "charArrayDensity",
                new DensityMetricValue(r.char_array_accesses,
                        inst_count)));
        visitor.visit(new ValueMetric(CATEGORY, "numArrayDensity",
                new DensityMetricValue(r.num_array_accesses,
                        inst_count)));
        visitor.visit(new ValueMetric(CATEGORY, "refArrayDensity",
                new DensityMetricValue(r.ref_array_accesses,
                        inst_count)));
    }
    
    public MetricRecord newRecord() {
        return new ArrayMetricsRecord();
    }
    
    private class ArrayMetricsRecord implements MetricRecord {
        public long total_inst_count;
        public long total_array_accesses;
        public long char_array_accesses;
        public long num_array_accesses;
        public long ref_array_accesses;
    }
}
