package starj.spec;

import java.util.*;
import java.util.regex.*;

public class EventDefinition {
    private String name;
    private int id;
    private Map fields;
    
    public EventDefinition(String name, int id) {
        this.name = name;
        this.id = id;
        this.fields = new HashMap();
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    public void addField(FieldDefinition field) {
        String name = field.getName();
        if (this.fields.containsKey(name)) {
            throw new RuntimeException("Field name '" + name +
                    "' already exists");
        }

        this.fields.put(name, field);
    }

    public boolean containsField(String name) {
        return this.fields.containsKey(name);
    }

    public FieldDefinition getByName(String name) {
        return (FieldDefinition) this.fields.get(name);
    }
    
    public FieldDefinition getByMask(int mask) {
        for (Iterator i = this.fields.values().iterator(); i.hasNext(); ) {
            FieldDefinition def = (FieldDefinition) i.next();
            if (def.getMask() == mask) {
                return def;
            }
        }
        
        return null;
    }

    public FieldDefinition[] getFields() {
        List l = new ArrayList(this.fields.values());
        FieldDefinition[] rv = new FieldDefinition[l.size()];
        rv = (FieldDefinition[]) l.toArray(rv);
        return rv;
    }

    public FieldDefinition[] getMatching(String pattern) {
        // Translate pattern to reg. expression
        String re = pattern.replaceAll("\\*", "[a-zA-Z_]*");
        Pattern p = Pattern.compile(re);

        List result = new LinkedList();

        for (Iterator i = this.fields.values().iterator(); i.hasNext(); ) {
            FieldDefinition def = (FieldDefinition) i.next();
            String field_name = def.getName();
            Matcher m = p.matcher(field_name);
            if (m.matches()) {
                result.add(def);
            }
        }

        FieldDefinition[] rv = new FieldDefinition[result.size()];
        return (FieldDefinition[]) result.toArray(rv);
    }
}
