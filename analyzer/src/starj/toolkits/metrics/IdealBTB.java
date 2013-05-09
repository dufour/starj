package starj.toolkits.metrics;

import starj.toolkits.services.MethodEntity;
import starj.coffer.Method;

public class IdealBTB implements TargetCache {
    private long cache_misses;
    private Method cached_method;
    
    public IdealBTB() {
        this.cache_misses = 0L;
    }

    public void accept(MethodEntity target) {
        Method target_method = target.getMethod();
        if (this.cached_method == null
                || !this.cached_method.equals(target_method)) {
            this.cache_misses++;
            this.cached_method = target_method;
        }
    }

    public long getMissCount() {
        return this.cache_misses;
    }
}
