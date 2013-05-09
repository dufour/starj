package starj.toolkits.metrics;

import java.util.*;

import starj.EventBox;
import starj.coffer.*;
import starj.coffer.types.Type;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;
import starj.toolkits.services.*;
import starj.util.*;

public class PolymorphismMetrics extends AbstractMetricOperation {
    public static final String CATEGORY = "polymorphism";
    private static final int ARITY_BIN_COUNT = 3;
    private InstructionStartEvent inst_event; // cached event needed to request
                                              // a list of metric spaces for
                                              // a given call site

    public PolymorphismMetrics(String name, String description) {
        super(name, description);
        this.inst_event = new InstructionStartEvent();
    }

    public OperationSet operationDependencies() {
        OperationSet dep_set = super.operationDependencies(); 
        dep_set.add(CallSiteResolver.v());

        return dep_set;
    }

    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();
        dep_set.add(new EventDependency(
                Event.METHOD_ENTRY2, // Need receiver ID
                new TotalMask(Constants.FIELD_RECORDED
                    | Constants.FIELD_METHOD_ID
                    | Constants.FIELD_OBJ_ID),
                true));

        return dep_set;
    }

    public void apply(EventBox box, MetricRecord[] records) {
        MethodEntry2Event e = (MethodEntry2Event) box.getEvent();
        InstructionContext ic = e.getCallSiteContext();
        if (ic == null) {
            return;
        }
        
        InvokeInstruction call_site = (InvokeInstruction) ic.getInstruction();
        if (call_site == null || !call_site.isDynamic()) {
            return;
        }
        InstructionStartEvent inst_event = this.inst_event;
        ExecutionContext context = ic.getExecutionContext();
        inst_event.setEnvID(context.getThread().getID());
        inst_event.setMethodID(context.getMethod().getID());
        inst_event.setOffset(call_site.getOffset());
        MetricRecord[] cs_records = this.getMetricRecords(inst_event);

        for (int i = 0; i < cs_records.length; i++) {
            PolymorphismMetricsRecord r
                = (PolymorphismMetricsRecord) cs_records[i];
            r.total_calls++;
            
            CallSiteRecord csr = (CallSiteRecord) call_site.getStorageSpace(
                    r.storage_index);
            if (csr == null) {
                csr = new CallSiteRecord(call_site);
                call_site.setStorageSpace(r.storage_index, csr);
            }
            r.call_site_records.add(csr);
    
            // Receiver
            ObjectEntity obj_entity = IDResolver.v().getObjectEntity(
                    e.getObjectID());
            Type t = (obj_entity != null ? obj_entity.getType() : null);
            if (t != null) {
                csr.accept(t);
            }
    
            // Target
            MethodEntity me = IDResolver.v().getMethodEntity(e.getMethodID());
            if (me != null) {
                csr.accept(me);
            }
        }
    }

    private Bin makeBin(int index, MetricValue value) {
        if (index == 2) {
            return new RangeBin(IntegerKey.v(3), PositiveInfinityKey.v(),
                    value);
        } else {
            return new SimpleBin(IntegerKey.v(index + 1), value);
        }
    }

    public void accept(MetricVisitor visitor, MetricRecord record) {
        int total_call_sites = 0;
        long total_calls = 0L;
        long[] receiver_arity_bin = new long[ARITY_BIN_COUNT];
        long[] receiver_arity_calls_bin = new long[ARITY_BIN_COUNT];
        long[] target_arity_bin = new long[ARITY_BIN_COUNT];
        long[] target_arity_calls_bin = new long[ARITY_BIN_COUNT];
        long total_receiver_cache_misses = 0L;
        long total_target_cache_misses = 0L;
        
        PolymorphismMetricsRecord r = (PolymorphismMetricsRecord) record;

        /* Compute raw results */
        for (Iterator i = r.call_site_records.iterator(); i.hasNext(); ) {
            CallSiteRecord csr = (CallSiteRecord) i.next();

            // Receiver
            Type[] receiver_types = csr.getReceiverTypes();
            int receiver_bin_index = -1;
            switch (receiver_types.length) {
                case 0:
                    /* Ignore this call site. Empirical studies
                       show that this is (most likely) the result
                       of some optimization which removes
                       invokevirtuals which point to an
                       empty method. */
                    break;
                case 1:
                    receiver_bin_index = 0;
                    break;
                case 2:
                    receiver_bin_index = 1;
                    break;
                default:
                    receiver_bin_index = 2;
                    break;
            }

            // Target
            MethodEntity[] targets = csr.getTargets();
            int target_bin_index = -1;
            switch (targets.length) {
                case 0:
                    /* Ignore this call site. Empirical studies
                       show that this is (most likely) the result
                       of some optimization which removes
                       invokevirtuals which point to an
                       empty method. */
                    break;
                case 1:
                    target_bin_index = 0;
                    break;
                case 2:
                    target_bin_index = 1;
                    break;
                default:
                    target_bin_index = 2;
                    break;
            }

            /* DEBUGGING
            if (bin_index > 0) {
                System.err.println("-----------------------------");
                System.err.println("-> " + r.getCallSite());
                for (int j = 0; j < receiver_types.length; j++) {
                    System.err.println("  " + receiver_types[j]);
                }
                System.err.println("-----------------------------");
            }
            */

            long calls = csr.getCallsCount();
            if (receiver_bin_index >= 0) {
                receiver_arity_bin[receiver_bin_index] += 1;
                receiver_arity_calls_bin[receiver_bin_index] += calls;
                if (target_bin_index >= 0) {
                    target_arity_bin[target_bin_index] += 1;
                    target_arity_calls_bin[target_bin_index] += calls;
                }
                total_call_sites += 1;
                total_calls += calls;
                total_receiver_cache_misses
                        += csr.getReceiverCache().getMissCount();
                total_target_cache_misses
                        += csr.getTargetCache().getMissCount();
            }
        }


        /* Compute metrics */
        
        // polymorphism.calls.value
        visitor.visit(new ValueMetric(CATEGORY, "calls",
                new LongMetricValue(r.total_calls)));
        // polymorphism.callSites.value
        visitor.visit(new ValueMetric(CATEGORY, "callSites",
                    new IntMetricValue(r.call_site_records.size())));

        long inst_count = InstructionCountManager.v().getInstructionCount(
                record);
        visitor.visit(new ValueMetric(CATEGORY, "invokeDensity",
                    new DensityMetricValue(r.total_calls,
                            inst_count)));

        BinMetric m;
        // polymorphism.receiverArity.bin
        m = new BinMetric(CATEGORY, "receiverArity");
        for (int i = 0; i < ARITY_BIN_COUNT; i++) {
            m.addBin(this.makeBin(i, new PercentageMetricValue(
                    receiver_arity_bin[i], total_call_sites)));
        }
        visitor.visit(m);

        // polymorphism.targetArity.bin
        m = new BinMetric(CATEGORY, "targetArity");
        for (int i = 0; i < ARITY_BIN_COUNT; i++) {
            m.addBin(this.makeBin(i, new PercentageMetricValue(
                    target_arity_bin[i], total_call_sites)));
        }
        visitor.visit(m);

        // polymorphism.receiverArityCalls.bin
        m = new BinMetric(CATEGORY, "receiverArityCalls");
        for (int i = 0; i < ARITY_BIN_COUNT; i++) {
            m.addBin(this.makeBin(i, new PercentageMetricValue(
                    receiver_arity_calls_bin[i], total_calls)));
        }
        visitor.visit(m);

        // polymorphism.targetArityCalls.bin
        m = new BinMetric(CATEGORY, "targetArityCalls");
        for (int i = 0; i < ARITY_BIN_COUNT; i++) {
            m.addBin(this.makeBin(i, new PercentageMetricValue(
                    target_arity_calls_bin[i], total_calls)));
        }
        visitor.visit(m);

        // polymorphism.receiverPolyDensity.value
        visitor.visit(new ValueMetric(CATEGORY, "receiverPolyDensity",
                new DoubleMetricValue(receiver_arity_bin[1]
                        + receiver_arity_bin[2], total_call_sites)));

        // polymorphism.targetPolyDensity.value
        visitor.visit(new ValueMetric(CATEGORY, "targetPolyDensity",
                new DoubleMetricValue(target_arity_bin[1]
                        + target_arity_bin[2], total_call_sites)));

        // polymorphism.receiverPolyDensityCalls.value
        visitor.visit(new ValueMetric(CATEGORY, "receiverPolyDensityCalls",
                new DoubleMetricValue(receiver_arity_calls_bin[1]
                        + receiver_arity_calls_bin[2], total_calls)));

        // polymorphism.targetPolyDensityCalls.value
        visitor.visit(new ValueMetric(CATEGORY, "targetPolyDensityCalls",
                new DoubleMetricValue(target_arity_calls_bin[1]
                        + target_arity_calls_bin[2], total_calls)));

        // polymorphism.receiverCacheMissRate.value
        visitor.visit(new ValueMetric(CATEGORY, "receiverCacheMissRate",
                new PercentageMetricValue(total_receiver_cache_misses,
                        total_calls)));

        // polymorphism.receiverCacheMissRate.value
        visitor.visit(new ValueMetric(CATEGORY, "targetCacheMissRate",
                new PercentageMetricValue(total_target_cache_misses,
                        total_calls)));
    }

    private class CallSiteRecord {
        private InvokeInstruction call_site;
        private ReceiverCache receiver_cache;
        private TargetCache target_cache;
        private ObjectToLongHashMap receiver_type_counts;
        private ObjectToLongHashMap target_counts;

        public CallSiteRecord(InvokeInstruction call_site) {
            this.call_site = call_site;
            this.receiver_cache = new InlineCache();
            this.target_cache = new IdealBTB();
            this.receiver_type_counts = new ObjectToLongHashMap();
            this.target_counts = new ObjectToLongHashMap();
        }

        public InvokeInstruction getCallSite() {
            return this.call_site;
        }

        public ReceiverCache getReceiverCache() {
            return this.receiver_cache;
        }

        public TargetCache getTargetCache() {
            return this.target_cache;
        }

        public void accept(Type receiver) {
            this.receiver_cache.accept(receiver);
            this.receiver_type_counts.step(receiver);
        }

        public void accept(MethodEntity target) {
            this.target_cache.accept(target);
            this.target_counts.step(target);
        }

        public Type[] getReceiverTypes() {
            Object[] keys = this.receiver_type_counts.keySet();
            Type[] rv = new Type[keys.length];
            System.arraycopy(keys, 0, rv, 0, keys.length);
            return rv;
        }

        public MethodEntity[] getTargets() {
            Object[] keys = this.target_counts.keySet();
            MethodEntity[] rv = new MethodEntity[keys.length];
            System.arraycopy(keys, 0, rv, 0, keys.length);
            return rv;
        }

        public long getCallsCount() {
            long[] counts =this.receiver_type_counts.valueSet();
            long rv = 0L;
            for (int i = 0; i < counts.length; i++) {
                rv += counts[i];
            }

            return rv;
        }
    }
    
    public MetricRecord newRecord() {
        return new PolymorphismMetricsRecord();
    }
    
    private class PolymorphismMetricsRecord implements MetricRecord {
        public Set call_site_records;
        public long total_calls;
        public int storage_index;
        
        PolymorphismMetricsRecord() {
            this.call_site_records = new HashSet();
            this.total_calls = 0L;
            this.storage_index = Instruction.registerStorageSpace();
        }
    }
}
