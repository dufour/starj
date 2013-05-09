package starj.toolkits.metrics;

public class NegativeInfinityKey extends InfinityKey {
    private static NegativeInfinityKey instance = null;

    private NegativeInfinityKey() {
        // no instances
    }

    public int hashCode() {
        return 0x5AB1E11F; // Arbitrary hash code
    }

    public boolean equals(Object obj) {
        return (obj instanceof NegativeInfinityKey);
    }
    
    public String toString() {
        return "-Inf";
    }

    public static NegativeInfinityKey v() {
        if (instance == null) {
            instance = new NegativeInfinityKey();
        }
        
        return instance;
    }    
}
