package starj.toolkits.services;

import starj.coffer.types.Type;

public abstract class TypedEntity extends Entity {
    private Type type;
    
    public TypedEntity(int id, boolean standard_lib, Type type) {
        super(id, standard_lib);
        this.type = type;
    }

    public TypedEntity(int id, boolean standard_lib, Type type,
            boolean authoritative) {
        super(id, standard_lib, authoritative);
        this.type = type;
    }

    void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public String toString() {
        return String.valueOf(this.type);
    }
}
