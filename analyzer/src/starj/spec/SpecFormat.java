package starj.spec;

import java.util.*;
import java.util.regex.*;

public class SpecFormat {
    private Map events;

    public SpecFormat() {
        this.events = new HashMap();
    }

    public void addEvent(EventDefinition event) {
        String name = event.getName();
        if (this.events.containsKey(name)) {
            throw new RuntimeException("Event name '" + name +
                    "' already exists");
        }

        this.events.put(name, event);
    }

    public void addAll(FieldDefinition field) {
        for (Iterator i = this.events.values().iterator(); i.hasNext(); ) {
            ((EventDefinition) i.next()).addField(field);
        }
    }

    public EventDefinition getByName(String name) {
        return (EventDefinition) this.events.get(name);
    }
    
    public EventDefinition getByID(int id) {
        for (Iterator i = this.events.values().iterator(); i.hasNext(); ) {
            EventDefinition def = (EventDefinition) i.next();
            if (def.getID() == id) {
                return def;
            }
        }
        
        return null;
    }

    public EventDefinition[] getMatching(String pattern) {
        // Translate pattern to reg. expression
        String re = pattern.replaceAll("\\*", "[a-zA-Z_]*");
        Pattern p = Pattern.compile(re);

        List result = new LinkedList();

        for (Iterator i = this.events.values().iterator(); i.hasNext(); ) {
            EventDefinition def = (EventDefinition) i.next();
            String event_name = def.getName();
            Matcher m = p.matcher(event_name);
            if (m.matches()) {
                result.add(def);
            }
        }

        EventDefinition[] rv = new EventDefinition[result.size()];
        return (EventDefinition[]) result.toArray(rv);
    }

    public boolean containsEvent(String name) {
        return this.events.containsKey(name);
    }

    public EventDefinition[] getEvents() {
        List l = new ArrayList(this.events.values());
        EventDefinition[] rv = new EventDefinition[l.size()];
        rv = (EventDefinition[]) l.toArray(rv);
        return rv;
    }

    public FieldDefinition[] getMatchingFields(String pattern) {
        Set result = new HashSet();
        for (Iterator i = this.events.values().iterator(); i.hasNext(); ) {
            EventDefinition event_def = (EventDefinition) i.next();
            FieldDefinition[] field_defs = event_def.getMatching(pattern);
            for (int j = 0; j < field_defs.length; j++) {
                result.add(field_defs[j]);
            }
        }

        FieldDefinition[] rv = new FieldDefinition[result.size()];
        return (FieldDefinition[]) result.toArray(rv);
    }
}
