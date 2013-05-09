package starj.coffer.types;

public class VoidType extends BasicType {
    public String toTypeString() {
        return "V";
    }

    public String toString() {
        return "void";
    }

    public boolean equals(Object obj) {
        return (obj instanceof VoidType);
    }

    public int compareTo(Object obj) {
        if (obj instanceof VoidType) {
            return 0;
        } else if (obj instanceof Type) {
            return hashCode() - obj.hashCode();
        }

        throw new RuntimeException("Incompatible objects");
    }
    
    /* Experimental -- May be removed from the StarJ API at any time !! */
    public final int getTypeID() {
        return Type.VOID_TYPE;
    }
}
