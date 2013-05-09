package starj;

import starj.events.Event;

public class SingleEventBox implements EventBox {
    private Event event;

    public SingleEventBox() {
        this(null);
    }

    public SingleEventBox(Event event) {
        this.event = event;
    }
    
    public Event getEvent() {
        return this.event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    
    public void remove() {
        throw new EventSkipException();
    }
}
