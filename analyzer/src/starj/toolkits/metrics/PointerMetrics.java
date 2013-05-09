package starj.toolkits.metrics;

import starj.EventBox;
import starj.coffer.*;
import starj.coffer.types.Type;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;
import starj.toolkits.services.InstructionResolver;

public class PointerMetrics extends AbstractMetricOperation {
    public static final String CATEGORY = "pointer";

    public PointerMetrics(String name, String description) {
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
            case Code.GETFIELD:
            case Code.PUTFIELD:
            case Code.GETSTATIC:
            case Code.PUTSTATIC: {
                    FieldInstruction inst
                            = (FieldInstruction) e.getInstruction();
                    if (inst != null) {
                        Type t = inst.getType();
                        int tID = t.getTypeID();
                        for (int i = 0; i < records.length; i++) {
                            PointerMetricsRecord r
                                    = (PointerMetricsRecord) records[i];
                            switch (tID) {
                                case Type.OBJECT_TYPE:
                                case Type.ARRAY_TYPE:
                                    r.ref_field_accesses++;
                                    break;
                                default:
                                    r.nonref_field_accesses++;
                                    break;
                            }
                        }
                    }
                }
                break;
            default:
                /* Not a field instruction */
                break;
        }
    }

    public void accept(MetricVisitor visitor, MetricRecord record) {
        PointerMetricsRecord r = (PointerMetricsRecord) record;
        long inst_count = InstructionCountManager.v().getInstructionCount(
                record);
        visitor.visit(new ValueMetric(CATEGORY, "fieldAccessDensity",
                new DensityMetricValue(r.ref_field_accesses
                        + r.nonref_field_accesses, inst_count)));
        visitor.visit(new ValueMetric(CATEGORY, "refFieldAccessDensity",
                new DensityMetricValue(r.ref_field_accesses, inst_count)));
        visitor.visit(new ValueMetric(CATEGORY, "nonrefFieldAccessDensity",
                new DensityMetricValue(r.nonref_field_accesses, inst_count)));
    }
    
    public MetricRecord newRecord() {
        return new PointerMetricsRecord();
    }
    
    private class PointerMetricsRecord implements MetricRecord {
        public long ref_field_accesses;
        public long nonref_field_accesses;
    }
}
