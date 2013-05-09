package starj.coffer.types;

public class DoubleType extends BasicType {
    public String toTypeString() {
        return "D";
    }

    public String toString() {
        return "double";
    }

    public boolean equals(Object obj) {
        return (obj instanceof DoubleType);
    }

    public int compareTo(Object obj) {
        if (obj instanceof DoubleType) {
            return 0;
        } else if (obj instanceof Type) {
            return hashCode() - obj.hashCode();
        }

        throw new RuntimeException("Incompatible objects");
    }

    /* Experimental -- May be removed from the StarJ API at any time !! */
    public final int getTypeID() {
        return Type.DOUBLE_TYPE;
    }
}
