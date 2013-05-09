package starj.toolkits.aspects;

import starj.EventBox;
import starj.coffer.*;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;
import starj.toolkits.metrics.*;
import starj.toolkits.services.InstructionResolver;

public class AspectMetrics extends AbstractMetricOperation {
    public static final String CATEGORY = "aspects";

    public AspectMetrics(String name, String description) {
        super(name, description);
    }
    
    public OperationSet operationDependencies() {
        OperationSet dep_set = super.operationDependencies();
        dep_set.add(InstructionResolver.v());
        dep_set.add(AspectTagResolver.v());

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

    public void apply(EventBox box, MetricRecord records[]) {
        Event event = box.getEvent();
        InstructionStartEvent e = ((InstructionStartEvent) event);
        int env_id = e.getEnvID();
        int opcode = e.getOpcode();
        Instruction inst = e.getInstruction();
        
        int kind_tag;
        if (inst != null) {
            kind_tag = AspectTagResolver.v().getCurrentKindTag(inst, env_id);
        } else {
            // null instruction means that the class file could not be resolved.
            // This is known to happen for programs which use dynamically
            // dynamically generated classes.
            kind_tag = AspectTagResolver.ASPECT_TAG_NO_TAG;
        }

        if (kind_tag == AspectTagResolver.ASPECT_TAG_NO_TAG) {
            kind_tag = AspectTagResolver.ASPECT_TAG_BASE_CODE;
        }

        boolean is_alloc_site;
        switch (opcode) {
            case Code.NEW:
            case Code.NEWARRAY:
            case Code.ANEWARRAY:
            case Code.MULTIANEWARRAY:
                is_alloc_site = true;
                break;
            default:
                is_alloc_site = false;
                break;
        }

        for (int i = 0; i < records.length; i++) {
            AspectMetricsRecord r = (AspectMetricsRecord) records[i];
            r.kind_tag_counts[kind_tag] += 1L;
            if (is_alloc_site) {
                r.alloc_kind_tag_counts[kind_tag] += 1L;
            }
            
        }
    }
    
    public void accept(MetricVisitor visitor, MetricRecord record) {
        AspectMetricsRecord r = (AspectMetricsRecord) record;
        long total;
        
        total = 0L;
        for (int i = 0; i < AspectTagResolver.ASPECT_TAG_COUNT; i++) {
            total += r.kind_tag_counts[i];
        }
        
        // aspects.tagMix[Counts].bin
        BinMetric tag_mix = new BinMetric(CATEGORY, "tagMix");
        BinMetric tag_mix_counts = new BinMetric(CATEGORY, "tagMixCounts");
        for (int i = 0; i < AspectTagResolver.ASPECT_TAG_COUNT; i++) {
            String name = AspectTagResolver.TAG_NAMES[i];
            long l = r.kind_tag_counts[i];
            BinKey name_key = new StringKey(name);
            tag_mix.addBin(new SimpleBin(name_key,
                    new PercentageMetricValue(l, total)));
            tag_mix_counts.addBin(new SimpleBin(name_key,
                    new LongMetricValue(l)));
        }
        visitor.visit(tag_mix);
        visitor.visit(tag_mix_counts);
        
        long base_code_count
                = r.kind_tag_counts[AspectTagResolver.ASPECT_TAG_BASE_CODE];
        long aspect_code_count
                = r.kind_tag_counts[AspectTagResolver.ASPECT_TAG_ASPECT_CODE];
        // aspects.executionOverhead.value
        visitor.visit(new ValueMetric(CATEGORY, "executionOverhead", 
                new DoubleMetricValue(
                        total - base_code_count - aspect_code_count, total)));

        // aspects.adviceToOverheadRatio.value
        visitor.visit(new ValueMetric(CATEGORY, "adviceToOverheadRatio",
                new DoubleMetricValue(aspect_code_count,
                        total - aspect_code_count - base_code_count)));

        // aspects.adviceToApplicationRatio.value
        visitor.visit(new ValueMetric(CATEGORY, "adviceToApplicationRatio",
                new DoubleMetricValue(aspect_code_count, base_code_count)));

        // aspects.overheadToAdviceRatio.value
        visitor.visit(new ValueMetric(CATEGORY, "overheadToAdviceRatio",
                new DoubleMetricValue(
                        total - aspect_code_count - base_code_count,
                        aspect_code_count)));

        // aspects.adviceToTotalRatio.value
        visitor.visit(new ValueMetric(CATEGORY, "adviceToTotalRatio",
                new DoubleMetricValue(aspect_code_count, total)));

        total = 0L;
        for (int i = 0; i < AspectTagResolver.ASPECT_TAG_COUNT; i++) {
            total += r.alloc_kind_tag_counts[i];
        }
        
        // aspects.allocTagMix[Counts].bin
        BinMetric alloc_tag_mix = new BinMetric(CATEGORY, "allocTagMix");
        BinMetric alloc_tag_mix_counts
                = new BinMetric(CATEGORY, "allocTagMixCounts");
        for (int i = 0; i < AspectTagResolver.ASPECT_TAG_COUNT; i++) {
            String name = AspectTagResolver.TAG_NAMES[i];
            long l = r.alloc_kind_tag_counts[i];
            BinKey name_key = new StringKey(name);
            alloc_tag_mix.addBin(new SimpleBin(name_key,
                    new PercentageMetricValue(l, total)));
            alloc_tag_mix_counts.addBin(new SimpleBin(name_key,
                    new LongMetricValue(l)));
        }
        visitor.visit(alloc_tag_mix);
        visitor.visit(alloc_tag_mix_counts);
    }
    

    public MetricRecord newRecord() {
        return new AspectMetricsRecord();
    }

    private class AspectMetricsRecord implements MetricRecord {
        public long[] kind_tag_counts;
        public long[] alloc_kind_tag_counts;

        public AspectMetricsRecord() {
            this.kind_tag_counts = new long[AspectTagResolver.ASPECT_TAG_COUNT];
            this.alloc_kind_tag_counts
                    = new long[AspectTagResolver.ASPECT_TAG_COUNT];
        }
    }
}
