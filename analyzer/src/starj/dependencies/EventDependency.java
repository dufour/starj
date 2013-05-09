package starj.dependencies;

public class EventDependency {
    private int event_id;
    private boolean required;
    private FieldMask mask;
    private EventDependency alternate;

    public EventDependency(int event_id, FieldMask mask) {
        this(event_id, mask, true, null);
    }

    public EventDependency(int event_id, FieldMask mask,
            EventDependency alternate) {
        this(event_id, mask, true, alternate);
    }
    
    public EventDependency(int event_id, FieldMask mask, boolean required) {
        this(event_id, mask, required, null);
    }

    public EventDependency(int event_id, FieldMask mask, boolean required,
            EventDependency alternate) {
        this.event_id = event_id;
        this.mask = mask;
        this.required = required;
        this.alternate = alternate;
    }

    public int getEventID() {
        return this.event_id;
    }

    public boolean hasAlternate() {
        return (this.alternate != null);
    }

    public EventDependency getAlternate() {
        return this.alternate;
    }

    public void setAlternate(EventDependency alternate) {
        this.alternate = alternate;
    }

    public boolean matches(int mask) {
        return this.mask.matches(mask);
    }

    public boolean isRequired() {
        return this.required;
    }
}
