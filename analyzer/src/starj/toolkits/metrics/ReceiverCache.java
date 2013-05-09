package starj.toolkits.metrics;

import starj.coffer.types.Type;

public interface ReceiverCache extends Cache {
    public void accept(Type type);
}
