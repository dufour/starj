package starj.toolkits.metrics;

import starj.coffer.types.Type;

public class InlineCache implements ReceiverCache {
    private long cache_misses;
    private Type cached_type;
    
    public InlineCache() {
        this.cache_misses = 0L;
    }

    public void accept(Type type) {
        if (this.cached_type == null || !this.cached_type.equals(type)) {
            this.cache_misses++;
            this.cached_type = type;
        }
    }

    public long getMissCount() {
        return this.cache_misses;
    }
}
