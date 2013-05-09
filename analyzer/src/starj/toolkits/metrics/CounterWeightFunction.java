package starj.toolkits.metrics;

import starj.Storage;
import starj.util.WeightFunction;

public class CounterWeightFunction implements WeightFunction {
    private int counter_index;

    public CounterWeightFunction(int counter_index) {
        this.counter_index = counter_index;
    }

    public long getValue(Object obj) {
        return ((Storage) obj).getCounter(this.counter_index);
    }
}
