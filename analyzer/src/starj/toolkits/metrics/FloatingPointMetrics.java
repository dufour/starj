package starj.toolkits.metrics;

import starj.EventBox;
import starj.coffer.*;
import starj.coffer.types.*;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;
import starj.toolkits.services.InstructionResolver;

public class FloatingPointMetrics extends AbstractMetricOperation {
    public static final String CATEGORY = "data";

    public FloatingPointMetrics(String name, String description) {
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
        
        for (int i = 0; i < records.length; i++) {
            FloatingPointMetricsRecord r
                    = (FloatingPointMetricsRecord) records[i];
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
                            switch (tID) {
                                case Type.FLOAT_TYPE:
                                    r.float_inst_count++;
                                    break;
                                case Type.DOUBLE_TYPE:
                                    r.double_inst_count++;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    break;
                case Code.D2F:
                case Code.D2I:
                case Code.D2L:
                case Code.DADD:
                case Code.DALOAD:
                case Code.DASTORE:
                case Code.DCMPG:
                case Code.DCMPL:
                case Code.DCONST_0:
                case Code.DCONST_1:
                case Code.DDIV:
                case Code.DLOAD:
                case Code.DLOAD_0:
                case Code.DLOAD_1:
                case Code.DLOAD_2:
                case Code.DLOAD_3:
                case Code.DMUL:
                case Code.DNEG:
                case Code.DREM:
                case Code.DRETURN:
                case Code.DSTORE:
                case Code.DSTORE_0:
                case Code.DSTORE_1:
                case Code.DSTORE_2:
                case Code.DSTORE_3:
                case Code.DSUB:
                    /* Double */
                    r.double_inst_count++;
                    break;
                case Code.F2D:
                case Code.F2I:
                case Code.F2L:
                case Code.FADD:
                case Code.FALOAD:
                case Code.FASTORE:
                case Code.FCMPG:
                case Code.FCMPL:
                case Code.FCONST_0:
                case Code.FCONST_1:
                case Code.FCONST_2:
                case Code.FDIV:
                case Code.FLOAD:
                case Code.FLOAD_0:
                case Code.FLOAD_1:
                case Code.FLOAD_2:
                case Code.FLOAD_3:
                case Code.FMUL:
                case Code.FNEG:
                case Code.FREM:
                case Code.FRETURN:
                case Code.FSTORE:
                case Code.FSTORE_0:
                case Code.FSTORE_1:
                case Code.FSTORE_2:
                case Code.FSTORE_3:
                case Code.FSUB:
                    /* Float */
                    r.float_inst_count++;
                    break;
                default:
                    break;
            }
        }
    }
    
    public MetricRecord newRecord() {
        return new FloatingPointMetricsRecord();
    }
    
    private class FloatingPointMetricsRecord implements MetricRecord {
        public long float_inst_count;
        public long double_inst_count;
    }

    public void accept(MetricVisitor visitor, MetricRecord record) {
        FloatingPointMetricsRecord r = (FloatingPointMetricsRecord) record;
        long inst_count = InstructionCountManager.v().getInstructionCount(
                record);
        visitor.visit(new ValueMetric(CATEGORY, "floatDensity",
                new DensityMetricValue(
                        r.float_inst_count + r.double_inst_count,
                        inst_count)));
    }
}
