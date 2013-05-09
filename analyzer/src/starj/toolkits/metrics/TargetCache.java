package starj.toolkits.metrics;

import starj.toolkits.services.MethodEntity;

public interface TargetCache extends Cache {
    public void accept(MethodEntity target);
}
