package starj.spec;

import java.util.*;

public class EventSpecification {
    private EventDefinition event;
    private List fields;

    public EventSpecification(EventDefinition event) {
        this.event = event;
        this.fields = new LinkedList();
    }

    public EventDefinition getDefinition() {
        return this.event;
    }

    public void add(FieldSpecification field) {
        this.fields.add(field);
    }
    
    public boolean contains(FieldDefinition field) {
        return (this.getByDefinition(field) != null);
    }

    public FieldSpecification getByDefinition(FieldDefinition field) {
        for (Iterator i = this.fields.iterator(); i.hasNext(); ) {
            FieldSpecification spec = (FieldSpecification) i.next();
            FieldDefinition f = spec.getDefinition();
            if (field == f || field.equals(f)) {
                return spec;
            }
        }
        
        return null;
    }

    public FieldSpecification[] getFields() {
        FieldSpecification[] rv = new FieldSpecification[this.fields.size()];
        rv = (FieldSpecification[]) this.fields.toArray(rv);
        return rv;
    }
}
