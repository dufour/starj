package starj.coffer.types;

public class IntType extends BasicType {
    public String toTypeString() {
        return "I";
    }

    public String toString() {
        return "int";
    }

    public boolean equals(Object obj) {
        return (obj instanceof IntType);
    }

    public int compareTo(Object obj) {
        if (obj instanceof IntType) {
            return 0;
        } else if (obj instanceof Type) {
            return hashCode() - obj.hashCode();
        }

        throw new RuntimeException("Incompatible objects");
    }

    /* Experimental -- May be removed from the StarJ API at any time !! */
    public final int getTypeID() {
        return Type.INT_TYPE;
    }
}
