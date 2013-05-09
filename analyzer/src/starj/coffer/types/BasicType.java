package starj.coffer.types;

public abstract class BasicType implements Type {
    public BasicType() {
        // Default, empty constructor
    }

    public abstract String toTypeString();
    public abstract int getTypeID();
    
    public int hashCode() {
        return this.getTypeID();
    }
}
