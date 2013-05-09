package starj.coffer.types;

public class ShortType extends BasicType {
    public String toTypeString() {
        return "S";
    }

    public String toString() {
        return "short";
    }

    public boolean equals(Object obj) {
        return (obj instanceof ShortType);
    }

    public int compareTo(Object obj) {
        if (obj instanceof ShortType) {
            return 0;
        } else if (obj instanceof Type) {
            return hashCode() - obj.hashCode();
        }

        throw new RuntimeException("Incompatible objects");
    }

    /* Experimental -- May be removed from the StarJ API at any time !! */
    public final int getTypeID() {
        return Type.SHORT_TYPE;
    }
}
