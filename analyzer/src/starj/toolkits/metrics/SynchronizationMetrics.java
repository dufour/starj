package starj.toolkits.metrics;

import java.util.*;

import starj.EventBox;
import starj.Storage;
import starj.StorageFactory;
import starj.coffer.*;
import starj.coffer.types.*;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;
import starj.toolkits.services.*;
import starj.util.*;

public class SynchronizationMetrics extends AbstractMetricOperation {
    public static final String CATEGORY = "concurrency";
    private InstructionStartEvent inst_event; // cached event needed to request
                                              // a list of metric spaces for
                                              // a given call site
    private MethodEntryEvent method_event;
    private HashMap location_to_site;
    
    public SynchronizationMetrics(String name, String description) {
        super(name, description);
    }

    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();
        dep_set.add(new EventDependency(
                Event.MONITOR_CONTENDED_ENTER,
                new TotalMask(Constants.FIELD_RECORDED
                        | Constants.FIELD_OBJECT),
                true));
        FieldMask method_mask = new TotalMask(
                Constants.FIELD_RECORDED
                | Constants.FIELD_METHOD_ID);
        dep_set.add(new EventDependency(
                Event.METHOD_ENTRY2,
                method_mask,
                true,
                new EventDependency(
                    Event.METHOD_ENTRY,
                    method_mask,
                    true)));
        dep_set.add(new EventDependency(
                Event.INSTRUCTION_START,
                new TotalMask(Constants.FIELD_RECORDED),
                true));

        return dep_set;
    }
                        
    public OperationSet operationDependencies() {
        OperationSet dep_set = super.operationDependencies();
        dep_set.add(InstructionResolver.v());
        dep_set.add(CallStackManager.v());

        return dep_set;
    }

    public void init() {
        this.location_to_site = new HashMap();
        this.inst_event = new InstructionStartEvent();
        this.method_event = new MethodEntryEvent();
    }

    public void apply(EventBox box, MetricRecord[] records) {
        Event event = box.getEvent();
        switch (event.getID()) {
            case Event.MONITOR_CONTENDED_ENTER: {
                    MonitorContendedEnterEvent e
                            = (MonitorContendedEnterEvent) event;
                    ObjectEntity obj_entity = IDResolver.v().getObjectEntity(
                            e.getObjectID());
                    
                    InstructionContext ic
                            = InstructionResolver.v().getCurrentContext(
                                    e.getEnvID());
                    if (ic == null) {
                        break;
                    }
                    
                    ExecutionContext context = ic.getExecutionContext();
                    Instruction inst = ic.getInstruction();
                    
                    if (inst == null) {
                        break;
                    }
                    
                    LockingSite site = null;
                    MetricRecord[] sync_records = null;
                    switch (inst.getOpcode()) {
                        case Code.MONITORENTER:
                            site = (LockingSite) this.location_to_site.get(inst);
                            if (site == null) {
                                site = new InstructionLockingSite(
                                        (MonitorEnterInstruction) inst);
                                this.location_to_site.put(inst, site);
                            }
                            
                            // Prepare 'fake' event to get records
                            InstructionStartEvent inst_event = this.inst_event;
                            inst_event.setEnvID(context.getThread().getID());
                            inst_event.setMethodID(context.getMethod().getID());
                            inst_event.setOffset(inst.getOffset());
                            sync_records = this.getMetricRecords(inst_event);
                            break;
                        case Code.INVOKEINTERFACE:
                        case Code.INVOKESPECIAL:
                        case Code.INVOKESTATIC:
                        case Code.INVOKEVIRTUAL:
                            if (obj_entity == null) {
                                break;
                            }
                            InvokeInstruction inv_inst
                                    = (InvokeInstruction) inst;
                            Method tm = inv_inst.getTargetMethod();
                            String method_sig = tm.getFullName();                            
                            Type t = obj_entity.getType();
                            if (t.getTypeID() == Type.OBJECT_TYPE) {
                                ObjectType ot = (ObjectType) t;
                                ClassFile c = Repository.v().lookup(
                                        ot.getClassName());
                                if (c == null) {
                                    break;
                                }
                                Method m = c.getMethod(method_sig);
                                if (m != null && m.isSynchronized()) {
                                    // Consider m to have an implicit initial
                                    // 'monitorenter' bytecode
                                    site = (LockingSite) this.location_to_site.get(m);
                                    if (site == null) {
                                        site = new MethodLockingSite(m); 
                                        this.location_to_site.put(m, site);
                                    }
                            
                                    // Prepare 'fake' event to get records
                                    MethodEntryEvent method_event
                                            = this.method_event;
                                    method_event.setEnvID(context.getThread().getID());
                                    method_event.setCallSiteContext(ic);
                                    method_event.setMethodID(
                                            IDResolver.v().getMethodID(m));
                                    sync_records = this.getMetricRecords(method_event);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    
                    if (site == null) {
                        break;
                    }

                    for(int i = 0; i < sync_records.length; i++) {
                        SynchronizationMetricsRecord r
                                = (SynchronizationMetricsRecord)
                                        records[i];
                        site.stepCounter(r.contention_count_index);
                        r.contention_count += 1L;
                    }
                }
                break;
            case Event.METHOD_ENTRY:
            case Event.METHOD_ENTRY2: {
                    MethodEvent e = (MethodEvent) event;
                    int method_id = e.getMethodID();
                    MethodEntity me = IDResolver.v().getMethodEntity(method_id);
                    if (me != null) {
                        Method m = me.getMethod();
                        if (m != null && m.isSynchronized()) {
                            LockingSite site = (LockingSite)
                                    this.location_to_site.get(m);
                            if (site == null) {
                                site = new MethodLockingSite(m);
                                this.location_to_site.put(m, site);
                            }
                            
                            for(int i = 0; i < records.length; i++) {
                                SynchronizationMetricsRecord r
                                        = (SynchronizationMetricsRecord)
                                                records[i];
                                r.monitorenter_count += 1L;
                                site.stepCounter(r.entry_count_index);
                            }
                        }
                    }
                }
                break;
            case Event.INSTRUCTION_START: {
                    InstructionStartEvent e = (InstructionStartEvent) event;
                    Instruction inst = e.getInstruction();
                    if (inst != null) {
                        if (inst.getOpcode() == Code.MONITORENTER) {
                            LockingSite site = (LockingSite)
                                    this.location_to_site.get(inst);
                            if (site == null) {
                                site = new InstructionLockingSite(
                                        (MonitorEnterInstruction) inst);
                                this.location_to_site.put(inst, site);
                            }
                            for(int i = 0; i < records.length; i++) {
                                SynchronizationMetricsRecord r
                                        = (SynchronizationMetricsRecord)
                                                records[i];
                                r.monitorenter_count += 1L;
                                site.stepCounter(r.entry_count_index);
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    public void accept(MetricVisitor visitor, MetricRecord record) {
        SynchronizationMetricsRecord r = (SynchronizationMetricsRecord) record;

        long inst_count = InstructionCountManager.v().getInstructionCount(
                record);
        visitor.visit(new ValueMetric(CATEGORY, "lockDensity",
                    new DensityMetricValue(r.monitorenter_count,
                            inst_count)));
        
        visitor.visit(new ValueMetric(CATEGORY, "contendedLockDensity",
                new DensityMetricValue(r.contention_count, inst_count)));
        
        float threshold = 0.90f;
        
        WeightFunction monitor_entry_function = new CounterWeightFunction(
                r.entry_count_index);
        Collection locking_sites = this.location_to_site.values();
        Collection touched_monitors = CollectionUtils.filter(
                locking_sites, new WeightFilter(monitor_entry_function));
        visitor.visit(new PercentileMetric(CATEGORY, "lock",
                new PercentageMetricValue(PercentileManager.getPercentile(
                        touched_monitors, monitor_entry_function,
                        threshold)), threshold));
        touched_monitors = null;
        
        WeightFunction contention_function
                = new CounterWeightFunction(r.contention_count_index);
        Collection contended_monitors = CollectionUtils.filter(
                locking_sites, new WeightFilter(contention_function));
        visitor.visit(new PercentileMetric(CATEGORY, "contendedLock",
                new PercentageMetricValue(PercentileManager.getPercentile(
                        contended_monitors, contention_function,
                        threshold)), threshold));
        contended_monitors = null;
    }
    
    public MetricRecord newRecord() {
        return new SynchronizationMetricsRecord();
    }
    
    private class SynchronizationMetricsRecord implements MetricRecord {
        public long monitorenter_count;
        public long contention_count;
        public int entry_count_index;
        public int contention_count_index;
        
        public SynchronizationMetricsRecord() {
            this.entry_count_index = LockingSite.registerCounter();
            this.contention_count_index = LockingSite.registerCounter();
        }
    }
    
    private static abstract class LockingSite implements Storage {
        private static StorageFactory storage_factory = new StorageFactory();
        private Storage storage;
        
        private Storage getStorage() {
            Storage rv = this.storage;
            if (rv == null) {
                rv = LockingSite.storage_factory.newStorage();
                this.storage = rv;
            }
            
            return rv;
        }
        
        public static StorageFactory getStorageFactory() {
            return LockingSite.storage_factory;
        }
        
        public static int registerFlag() {
            return LockingSite.storage_factory.registerFlag();
        }
        
        public static int registerCounter() {
            return LockingSite.storage_factory.registerCounter();
        }
        
        public static int registerStorageSpace() {
            return LockingSite.storage_factory.registerStorageSpace();
        }
        
        public boolean getFlag(int storage_id) {
            return this.getStorage().getFlag(storage_id);
        }
        
        public void setFlag(int storage_id, boolean value) {
            this.getStorage().setFlag(storage_id, value);
        }
        
        public long getCounter(int storage_id) {
            return this.getStorage().getCounter(storage_id);
        }
        
        public void setCounter(int storage_id, long value) {
            this.getStorage().setCounter(storage_id, value);
        }
        
        public void stepCounter(int storage_id) {
            this.getStorage().stepCounter(storage_id);
        }
        
        public void decrCounter(int storage_id) {
            this.getStorage().decrCounter(storage_id);
        }
        
        public void addToCounter(int storage_id, long delta) {
            this.getStorage().addToCounter(storage_id, delta);
        }
        
        public Object getStorageSpace(int storage_id) {
            return this.getStorage().getStorageSpace(storage_id);
        }
        
        public void setStorageSpace(int storage_id, Object value) {
            this.getStorage().setStorageSpace(storage_id, value);
        }
    }
    
    private static class InstructionLockingSite extends LockingSite {
        private MonitorEnterInstruction site;
        
        public InstructionLockingSite(MonitorEnterInstruction site) {
            this.site = site;
        }
        
        public MonitorEnterInstruction getSite() {
            return this.site;
        }
        
        /*
        public int hashCode() {
            return (this.site != null ? this.site.hashCode() : 0);
        }
        
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            
            if (!(obj instanceof InstructionLockingSite)) {
                return false;
            }
            
            InstructionLockingSite os = (InstructionLockingSite) obj;
            return this.site == os.site
                    || (this.site != null && this.site.equals(os.site));
        }
        */
        
        public String toString() {
            return String.valueOf(this.site);
        }
    }
    
    private static class MethodLockingSite extends LockingSite {
        private Method site;
        
        public MethodLockingSite(Method site) {
            this.site = site;
        }
        
        public Method getSite() {
            return this.site;
        }
        
        /*
        public int hashCode() {
            return (this.site != null ? this.site.hashCode() : 0);
        }
        
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            
            if (!(obj instanceof MethodLockingSite)) {
                return false;
            }
            
            MethodLockingSite os = (MethodLockingSite) obj;
            return this.site == os.site
                    || (this.site != null && this.site.equals(os.site));
        }
        */
        
        public String toString() {
            return String.valueOf(this.site);
        }
    }
}
