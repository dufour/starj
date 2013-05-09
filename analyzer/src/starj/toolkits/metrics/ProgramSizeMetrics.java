package starj.toolkits.metrics;

import java.util.*;

import starj.EventBox;
import starj.coffer.*;
import starj.dependencies.*;
import starj.events.*;
import starj.io.logging.LogManager;
import starj.spec.Constants;
import starj.toolkits.services.*;
import starj.util.*;

public class ProgramSizeMetrics extends AbstractMetricOperation {
    public static final String CATEGORY = "size";
    private Set touched_code;
    private Set loaded_methods;
    private Set loaded_classes;

    public ProgramSizeMetrics(String name, String description) {
        super(name, description);
    }

    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();
        dep_set.add(new EventDependency(
                Event.CLASS_LOAD,
                new TotalMask(Constants.FIELD_RECORDED
                        | Constants.FIELD_CLASS_NAME),
                true));
        dep_set.add(new EventDependency(
                Event.INSTRUCTION_START,
                new TotalMask(Constants.FIELD_RECORDED),
                true));

        return dep_set;
    }


    public OperationSet operationDependencies() {
        OperationSet dep_set = super.operationDependencies();
        dep_set.add(InstructionResolver.v());

        return dep_set;
    }

    public void init() {
        this.loaded_classes = new HashSet();
        this.loaded_methods = new HashSet();
        this.touched_code = new HashSet();
    }

    public void apply(EventBox box, MetricRecord[] records) {
        Event event = box.getEvent();
        switch (event.getID()) {
            case Event.CLASS_LOAD: {
                    ClassLoadEvent e = (ClassLoadEvent) event;
                    int class_id = e.getClassID(); 
                    ClassEntity class_entity
                            = IDResolver.v().getClassEntity(class_id);
                    if (class_entity == null) {
                        LogManager.v().logWarning("Failed to resolve class: "
                                + e.getClassName());
                        break;
                    }

                    int method_count = 0;
                    int code_size = 0;
                    ClassFile class_file = class_entity.getClassFile();
                    if (class_file != null) {
                        this.loaded_classes.add(class_file);
                    }
                    
                    
                    MethodEntity[] methods = class_entity.getMethods();
                    if (methods != null) {
                        method_count = methods.length;
                        for (int j = 0; j < method_count; j++) {
                            MethodEntity me = methods[j];
                            Method m = me.getMethod();
                            this.loaded_methods.add(m);
                            if (m != null) {
                                Code code = m.getCode();
                                if (code != null) {
                                    code_size += code.getSize();
                                    this.touched_code.add(code);
                                } else if (!(m.isNative()
                                        || m.isAbstract())) {
                                    LogManager.v().logWarning(
                                            "Failed to resolve method: "
                                            + me);
                                }
                            }
                        }
                    }

                    for (int i = 0; i < records.length; i++) {
                        ProgramSizeMetricsRecord r
                                = (ProgramSizeMetricsRecord) records[i];
                        
                        r.loaded_classes++;
                        r.loaded_methods += method_count;
                        r.loaded_insts += code_size;
                    }
                }
                break;
            case Event.INSTRUCTION_START: {
                    InstructionStartEvent e = (InstructionStartEvent) event;
                    Instruction inst = e.getInstruction();
                    if (inst != null) {
                        for (int i = 0; i < records.length; i++) {
                            ProgramSizeMetricsRecord r
                                    = (ProgramSizeMetricsRecord) records[i];
                            inst.stepCounter(r.touched_count_index);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    public void accept(MetricVisitor visitor, MetricRecord record) {
        ProgramSizeMetricsRecord r = (ProgramSizeMetricsRecord) record;
        visitor.visit(new ValueMetric(CATEGORY, "loadedClasses",
                new LongMetricValue(r.loaded_classes)));
        visitor.visit(new ValueMetric(CATEGORY, "loadedMethods",
                new LongMetricValue(r.loaded_methods)));
        visitor.visit(new ValueMetric(CATEGORY, "load",
                new LongMetricValue(r.loaded_insts)));
        float threshold = 0.90f;
        
        // Build a collection of touched instructions
        int counter_index = r.touched_count_index;
        Collection touched_insts = new LinkedList();
        for (Iterator i = this.touched_code.iterator(); i.hasNext(); ) {
            Code c = (Code) i.next();
            Instruction[] insts = c.getInstructions();
            if (insts != null) {
                for (int j = 0; j < insts.length; j++) {
                    Instruction inst = insts[j];
                    if (inst.getCounter(counter_index) > 0L) {
                        touched_insts.add(inst);
                    }
                }
            }
        }
        
        int touched_size = touched_insts.size();
        WeightFunction inst_weights
                = new CounterWeightFunction(r.touched_count_index);
        visitor.visit(new ValueMetric(CATEGORY, "run",
                    new LongMetricValue(touched_size)));
        long hot_size = PercentileManager.getHotValue(
                touched_insts,
                inst_weights,
                threshold);
        visitor.visit(new ValueMetric(CATEGORY, "hot",
                    new LongMetricValue(hot_size)));
        visitor.visit(new PercentileMetric(CATEGORY, "hot",
                new PercentageMetricValue(hot_size, touched_size),
                threshold)
        );
        
        WeightFunction method_weights = new MethodWeightFunction(inst_weights);
        Collection touched_methods = CollectionUtils.filter(
                this.loaded_methods, new WeightFilter(method_weights));
        long hot_methods = PercentileManager.getHotValue(touched_methods,
                method_weights, threshold);
        visitor.visit(new ValueMetric(CATEGORY, "hotMethods",
                new LongMetricValue(hot_methods)));
        visitor.visit(new PercentileMetric(CATEGORY, "hotMethods",
                new PercentageMetricValue(hot_methods, touched_methods.size()),
                threshold)
        );
        
        WeightFunction class_weights = new ClassWeightFunction(method_weights);
        Collection touched_classes = CollectionUtils.filter(
                this.loaded_classes, new WeightFilter(class_weights));
        long hot_classes = PercentileManager.getHotValue(touched_classes,
                class_weights, threshold);
        visitor.visit(new ValueMetric(CATEGORY, "hotClasses",
                new LongMetricValue(hot_classes)));
        visitor.visit(new PercentileMetric(CATEGORY, "hotClasses",
                new PercentageMetricValue(hot_classes, touched_classes.size()),
                threshold)
        );
                
        visitor.visit(new ValueMetric(CATEGORY, "deadCode",
                    new LongMetricValue(r.loaded_insts - touched_size)));
        visitor.visit(new ValueMetric(CATEGORY, "codeCoverage",
                    new PercentageMetricValue(touched_size, r.loaded_insts)));
    }
    
    public MetricRecord newRecord() {
        return new ProgramSizeMetricsRecord();
    }
    
    private class ProgramSizeMetricsRecord implements MetricRecord {
        public long loaded_classes;
        public long loaded_methods;
        public long loaded_insts;
        public int touched_count_index;
        
        public ProgramSizeMetricsRecord() {
            this.touched_count_index = Instruction.registerCounter();
        }
    }

    private class ClassWeightFunction implements WeightFunction {
        private WeightFunction method_weights;
        
        public ClassWeightFunction(WeightFunction method_weights) {
            this.method_weights = method_weights;
        }

        public long getValue(Object obj) {
            // TODO: add caching?
            long total_weight = 0L;
            if (obj == null) {
                return total_weight;
            }
            ClassFile c = (ClassFile) obj;
            Method[] methods = c.getMethods();
            if (methods != null) {
                for (int i = 0; i < methods.length; i++) {
                    total_weight += this.method_weights.getValue(methods[i]);
                }
            }
            return total_weight;
        }
    }

    private class MethodWeightFunction implements WeightFunction {
        private WeightFunction inst_weights;

        public MethodWeightFunction(WeightFunction inst_weights) {
            this.inst_weights = inst_weights;
        }
        
        public long getValue(Object obj) {
            // TODO: add caching?
            long total_weight = 0L;
            if (obj == null) {
                return total_weight;
            }
            
            Method m = (Method) obj;
            Code c = m.getCode();
            if (c != null) {
                Instruction[] insts = c.getInstructions();
                if (insts != null) {
                    for (int i = 0; i < insts.length; i++) {
                        total_weight += this.inst_weights.getValue(insts[i]);
                    }
                }
            }
            return total_weight;
        }
    }
}
