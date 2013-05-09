package starj.toolkits.metrics;

import starj.util.IntHashMap;

public class IntegerKey implements BinKey {
    private int value;
    private static IntHashMap cache = new IntHashMap(15);
    
    public IntegerKey(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public int hashCode() {
        return this.value;
    }

    public boolean equals(Object obj) {
        return (obj instanceof IntegerKey)
                && ((IntegerKey) obj).value == this.value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
    
    public static IntegerKey v(int value) {
        IntegerKey k = (IntegerKey) IntegerKey.cache.get(value);
        if (k == null) {
            k = new IntegerKey(value);
            IntegerKey.cache.put(value, k);
        }
        
        return k;
    }
    
    public String getType() {
        return "int";
    }
}
