package starj.spec;

public abstract class  AbstractTraceVisitor {
    public void visit(TraceSpecification trace) {
        EventSpecification[] events = trace.getEvents();
        if (events != null) {
            for (int i = 0; i < events.length; i++) {
                this.visitEvent(events[i]);
            }
        } 
    }

    protected void visitEvent(EventSpecification event) {
        FieldSpecification[] fields = event.getFields();
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                this.visitField(fields[i]);
            }
        }
    }
    
    protected abstract void visitField(FieldSpecification field);
}
