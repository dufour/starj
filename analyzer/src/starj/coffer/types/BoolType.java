package starj.coffer.types;

public class BoolType extends BasicType {
    public String toTypeString() {
        return "Z";
    }

    public String toString() {
        return "boolean";
    }

    public boolean equals(Object obj) {
        return (obj instanceof BoolType);
    }

    public int compareTo(Object obj) {
        if (obj instanceof BoolType) {
            return 0;
        } else if (obj instanceof Type) {
            return hashCode() - obj.hashCode();
        }

        throw new RuntimeException("Incompatible objects");
    }

    public final int getTypeID() {
        return Type.BOOL_TYPE;
    }
}
