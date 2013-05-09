package starj.spec;

import java.io.*;

public class PrettyPrinter extends AbstractTraceVisitor {
    private PrintStream out;

    public PrettyPrinter() {
        this(System.out);
    }

    public PrettyPrinter(PrintStream out) {
        this.out = out;
    }

    protected void visitEvent(EventSpecification event) {
        EventDefinition def = event.getDefinition();
        this.out.println("event " + def.getName() + " {");
        super.visitEvent(event);
        this.out.println("}\n");
    }
    
    protected void visitField(FieldSpecification field) {
        FieldDefinition def = field.getDefinition();
        this.out.println("    " + def.getName() + ": " + field.getValue()
                + ";");
    }

    public static void main(String[] args) {
        TraceSpecification trace = new TraceSpecification();
        EventSpecification spec;

        spec = new EventSpecification(new EventDefinition("event1", 1));
        spec.add(new FieldSpecification(new FieldDefinition("f1", 0x00000001), 
                true));
        spec.add(new FieldSpecification(new FieldDefinition("f2", 0x00000002), 
                false));
        trace.add(spec);

        spec = new EventSpecification(new EventDefinition("event2", 2));
        spec.add(new FieldSpecification(new FieldDefinition("f4", 0x00000004), 
                true));
        trace.add(spec);

        (new PrettyPrinter(System.out)).visit(trace);
    }
}
