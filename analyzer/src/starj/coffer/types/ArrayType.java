package starj.coffer.types;

public class ArrayType implements Type {
    private Type type;

    public ArrayType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public int compareTo(Object obj) {
        if (obj instanceof ArrayType) {
            return (this.type.compareTo(((ArrayType) obj).type));
        } else if (obj instanceof ObjectType) {
            return -1;
        } else if (obj instanceof Type) {
            return 1;
        }

        throw new RuntimeException("Incompatible object");
    }

    public boolean equals(Object obj) {
        return (obj instanceof ArrayType && compareTo(obj) == 0);
    }

    public int hashCode() {
        if (this.type == null) {
            return 0;
        } else {
            return this.type.hashCode();
        }
    }

    public String toTypeString() {
        return "[" + (this.type != null ? this.type.toTypeString() : null);
    }

    /* Experimental -- May be removed from the StarJ API at any time !! */
    public final int getTypeID() {
        return Type.ARRAY_TYPE;
    }

    public String toString() {
        return this.type + "[]";
    }
}
