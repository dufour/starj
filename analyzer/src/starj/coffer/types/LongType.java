package starj.coffer.types;

public class LongType extends BasicType {
    public String toTypeString() {
        return "J";
    }

    public String toString() {
        return "long";
    }

    public boolean equals(Object obj) {
        return (obj instanceof LongType);
    }

    public int compareTo(Object obj) {
        if (obj instanceof LongType) {
            return 0;
        } else if (obj instanceof Type) {
            return hashCode() - obj.hashCode();
        }

        throw new RuntimeException("Incompatible objects");
    }

    /* Experimental -- May be removed from the StarJ API at any time !! */
    public final int getTypeID() {
        return Type.LONG_TYPE;
    }
}
