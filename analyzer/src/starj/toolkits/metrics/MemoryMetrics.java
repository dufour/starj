package starj.toolkits.metrics;

import starj.EventBox;
import starj.dependencies.*;
import starj.events.*;
import starj.options.*;
import starj.spec.Constants;
import starj.toolkits.services.InstructionResolver;

public class MemoryMetrics extends AbstractMetricOperation {
    public static final String CATEGORY = "memory";
    private static final int DEFAULT_HEADER_SIZE = 8;  // default object header
                                                       // size, in bytes
    private static final int WORD_SIZE = 8;            // word size, in bytes
    // Bin factors: multipliers for subsequent range upper bounds
    private static final int[] BIN_FACTORS = {0, 1, 2, 3, 4, 8, 16, 48};

    private int header_size;
    private int[][] bin_ranges;
    private BinKey[][] bin_keys;
    // Computation state
    private long total_bytes;
    private long total_objects;
    private long[] object_size_bin;

    public MemoryMetrics(String name, String description) {
        this(name, description, DEFAULT_HEADER_SIZE);
    }

    public MemoryMetrics(String name, String description, int header_size) {
        super(name, description);
        this.header_size = header_size;
        int last_bound = this.header_size;
        this.bin_ranges = new int[BIN_FACTORS.length + 1][2];
        this.bin_keys = new BinKey[BIN_FACTORS.length + 1][2];
        for (int i = 0; i < BIN_FACTORS.length; i++) {
            this.bin_ranges[i][0] = last_bound;
            this.bin_keys[i][0] = IntegerKey.v(last_bound);
            last_bound = header_size + BIN_FACTORS[i] * WORD_SIZE;
            this.bin_ranges[i][1] = last_bound;
            this.bin_keys[i][1] = IntegerKey.v(last_bound);
            last_bound += WORD_SIZE;
        }
        this.bin_ranges[BIN_FACTORS.length][0] = last_bound;
        this.bin_keys[BIN_FACTORS.length][0] = IntegerKey.v(last_bound);
        this.bin_ranges[BIN_FACTORS.length][1] = Integer.MAX_VALUE;
        this.bin_keys[BIN_FACTORS.length][1] = PositiveInfinityKey.v();
    }
    
    public int getHeaderSize() {
        return this.header_size;
    }

    public void setHeaderSize(int size) {
        this.header_size = size;
    }

    public OperationSet operationDependencies() {
        OperationSet dep_set = super.operationDependencies();
        dep_set.add(InstructionResolver.v());

        return dep_set;
    }

    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();
        dep_set.add(new EventDependency(
                Event.OBJECT_ALLOC,
                new TotalMask(Constants.FIELD_RECORDED
                    | Constants.FIELD_SIZE),
                true));

        return dep_set;
    }

    public void init() {
        this.total_bytes = 0L;
        this.total_objects = 0L;
        this.object_size_bin = new long[this.bin_ranges.length];
    }

    public void apply(EventBox box, MetricRecord[] records) {
        ObjectAllocEvent e = (ObjectAllocEvent) box.getEvent();
        int size = e.getSize();

        for (int i = 0; i < records.length; i++) {
            MemoryMetricsRecord r = (MemoryMetricsRecord) records[i];
            r.total_objects += 1L;
            r.total_bytes += size;
            for (int j = 0; j < this.bin_ranges.length; j++) {
                if (size <= this.bin_ranges[j][1]) {
                    r.object_size_bin[j] += 1L;
                    break;
                }
            }
        }
    }

    public void accept(MetricVisitor visitor, MetricRecord record) {
        MemoryMetricsRecord r = (MemoryMetricsRecord) record;
        
        long inst_count = InstructionCountManager.v().getInstructionCount(
                record);
        visitor.visit(new ValueMetric(CATEGORY, "byteAllocationDensity",
                    new DensityMetricValue(r.total_bytes, inst_count)));
        visitor.visit(new ValueMetric(CATEGORY, "objectAllocationDensity",
                    new DensityMetricValue(r.total_objects, inst_count)));
        visitor.visit(new ValueMetric(CATEGORY, "averageObjectSize",
                    new DoubleMetricValue(r.total_bytes, r.total_objects)));
        BinMetric obj_size_bin = new BinMetric(CATEGORY, "objectSize");
        for (int i = 0; i < r.object_size_bin.length; i++) {
            obj_size_bin.addBin(new RangeBin(
                    this.bin_keys[i][0], this.bin_keys[i][1],
                    new PercentageMetricValue(r.object_size_bin[i],
                            r.total_objects)));
        }
        visitor.visit(obj_size_bin);
    }
    
    public void configure(ElementConfigArgument config, Object value) {
        String name = config.getName();
        if (name.equals("header_size")) {
            if (value != null) {
                this.header_size = ((Integer) value).intValue();
            }
        } else {
            super.configure(config, value);
        }
    }
    
    public ElementConfigSet getConfigurationSet() {
        ElementConfigSet set = super.getConfigurationSet();
        ElementConfigArgument header_size_arg = new ElementConfigArgument(
                0,
                "header_size",
                "Sets the object header size",
                "Sets the object header size (in bytes)",
                false
        );
        header_size_arg.addArgument(new IntArgument(
                0,
                "size",
                true,  // Required
                false, // Repeatable
                "int",
                "int (bytes)"
        ));
        set.addConfig(header_size_arg);
        return set;
    }
    
    public MetricRecord newRecord() {
        return new MemoryMetricsRecord();
    }
    
    private class MemoryMetricsRecord implements MetricRecord {
        public long total_bytes;
        public long total_objects;
        public long[] object_size_bin;
        
        MemoryMetricsRecord() {
            int bin_count = MemoryMetrics.BIN_FACTORS.length + 1;
            this.object_size_bin = new long[bin_count];
        }
    }
}
