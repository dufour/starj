package starj.spec;

import java.util.*;

public class TraceSpecification {
    private List events;
    
    public TraceSpecification() {
        this.events = new LinkedList();
    }
    
    public void add(EventSpecification event) {
        this.events.add(event);
    }

    public boolean contains(EventDefinition event) {
        return (this.getByDefinition(event) != null);
    }

    public EventSpecification getByDefinition(EventDefinition event) {
        for (Iterator i = this.events.iterator(); i.hasNext(); ) {
            EventSpecification spec = (EventSpecification) i.next();
            EventDefinition f = spec.getDefinition();
            if (event == f || event.equals(f)) {
                return spec;
            }
        }
        
        return null;
    }
    
    public EventSpecification[] getEvents() {
        EventSpecification[] rv = new EventSpecification[this.events.size()];
        rv = (EventSpecification[]) this.events.toArray(rv);
        return rv;
    }
}
