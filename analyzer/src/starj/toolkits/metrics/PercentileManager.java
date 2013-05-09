package starj.toolkits.metrics;

import java.util.*;

import starj.util.WeightFunction;

public class PercentileManager {
    private PercentileManager() {
        // no instances
    }

    public static long getHotValue(Collection collection, WeightFunction f,
            float threshold) {
        // Get an array instead of using iterators for efficiency since
        // we will do multiple passes on the entire collection
        Object[] objects = collection.toArray();
        int object_count = objects.length;

        // Sort the array in decreasing order (as defined by the
        // provided mapping)
        Arrays.sort(objects, new PercentileComparator(f));

        // Compute sum of all object values ...
        long total_size = 0L;
        for (int i = 0; i < object_count; i++) {
            total_size += f.getValue(objects[i]);
        }
        // ... from which we can compute a cutoff value which corresponds
        // to the given threshold (this saves a lot of FP operations)
        long cutoff = (long) Math.ceil(threshold * total_size);

        // Iterator over the objects, keeping track of the two quantities:
        //   - the current number of objects that we have processed
        //   - the current total of the processed objects' values
        long current_total = 0L;
        long result = 0L;        
        for(int i = 0; i < objects.length; i++) {
            if (current_total < cutoff) {
                long v = f.getValue(objects[i]);
            
                current_total += v;
                result++;
            } else {
                break;
            }
        }

        return result;
    }

    public static double getPercentile(Collection collection, WeightFunction f,
            float threshold) {
        return ((double) getHotValue(collection, f, threshold))
                / collection.size();
    }

    static class PercentileComparator implements Comparator {
        private WeightFunction f;
        
        PercentileComparator(WeightFunction f) {
            this.f = f;
        }

        public int compare(Object o1, Object o2) {
            long l1 = this.f.getValue(o1);
            long l2 = this.f.getValue(o2);

            if (l2 > l1) {
                return 1;
            }

            if (l2 == l1) {
                return 0;
            }

            return -1;
        }
    }
}
