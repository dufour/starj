package starj.coffer.types;

public class ObjectType implements Type {
    private String class_name;

    public ObjectType() {
        this.class_name = null;
    }

    public ObjectType(String class_name) {
        this.class_name = class_name;
    }

    public String getClassName() {
        return this.class_name;
    }

    public void setClassName(String class_name) {
        this.class_name = class_name;
    }
    
    public int compareTo(Object obj) {
        if (obj == this) {
            return 0;
        }
        
        if (obj == null) {
            return -1;
        }
        
        if (obj instanceof ObjectType) {
            ObjectType ot = (ObjectType) obj;

            if (this.class_name == null && ot.class_name != null) {
                return 1;
            }

            if (this.class_name != null && ot.class_name == null) {
                return -1;
            }

            if (this.class_name == ot.class_name) {
                return 0;
            }

            return this.class_name.compareTo(ot.class_name);
        } else if (obj instanceof Type) {
            return 1;
        }

        throw new RuntimeException("Incompatible object");
    }

    public boolean equals(Object obj) {
        return (obj instanceof ObjectType && compareTo(obj) == 0);
    }

    public int hashCode() {
        if (this.class_name == null) {
            return 0;
        } else {
            return this.class_name.hashCode();
        }
    }

    public String toTypeString() {
        return "L" + (this.class_name != null ?
                this.class_name.replace('.', '/') : this.class_name) + ";";
    }

    public String toString() {
        return (this.class_name != null
                ? this.class_name.replace('/', '.') : this.class_name);
    }

    /* Experimental -- May be removed from the StarJ API at any time !! */
    public final int getTypeID() {
        return Type.OBJECT_TYPE;
    }
}
