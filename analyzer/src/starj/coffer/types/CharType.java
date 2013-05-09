package starj.coffer.types;

public class CharType extends BasicType {
    public String toTypeString() {
        return "C";
    }
    
    public String toString() {
        return "char";
    }

    public boolean equals(Object obj) {
        return (obj instanceof CharType);
    }

    public int compareTo(Object obj) {
        if (obj instanceof CharType) {
            return 0;
        } else if (obj instanceof Type) {
            return hashCode() - obj.hashCode();
        }

        throw new RuntimeException("Incompatible objects");
    }


    /* Experimental -- May be removed from the StarJ API at any time !! */
    public final int getTypeID() {
        return Type.CHAR_TYPE;
    }
}
