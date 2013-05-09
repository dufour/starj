package starj.toolkits.metrics;

public class PositiveInfinityKey extends InfinityKey {
    private static PositiveInfinityKey instance = null;

    private PositiveInfinityKey() {
        // no instances
    }

    public int hashCode() {
        return 0x5AB1E01F; // Arbitrary hash code
    }

    public boolean equals(Object obj) {
        return (obj instanceof PositiveInfinityKey);
    }
    
    public String toString() {
        return "+Inf";
    }

    public static PositiveInfinityKey v() {
        if (instance == null) {
            instance = new PositiveInfinityKey();
        }
        
        return instance;
    }
}
