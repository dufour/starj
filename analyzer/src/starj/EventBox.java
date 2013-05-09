package starj;

import starj.events.Event;

public interface EventBox {
    public Event getEvent();
    public void setEvent(Event event);
    public void remove();
}
