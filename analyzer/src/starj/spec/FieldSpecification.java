package starj.spec;

public class FieldSpecification {
    private FieldDefinition field;
    private boolean value;

    public FieldSpecification(FieldDefinition field) {
        this(field, true);
    }

    public FieldSpecification(FieldDefinition field, boolean value) {
        this.field = field;
        this.value = value;
    }

    public FieldDefinition getDefinition() {
        return this.field;
    }

    public boolean getValue() {
        return this.value;
    }

    void setValue(boolean value) {
        this.value = value;
    }
}
