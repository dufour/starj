package starj.coffer.types;

public class ByteType extends BasicType {
    public String toTypeString() {
        return "B";
    }

    public String toString() {
        return "byte";
    }

    public boolean equals(Object obj) {
        return (obj instanceof ByteType);
    }

    public int compareTo(Object obj) {
        if (obj instanceof ByteType) {
            return 0;
        } else if (obj instanceof Type) {
            return hashCode() - obj.hashCode();
        }

        throw new RuntimeException("Incompatible objects");
    }
    
    /* Experimental -- May be removed from the StarJ API at any time !! */
    public final int getTypeID() {
        return Type.BYTE_TYPE;
    }
}
