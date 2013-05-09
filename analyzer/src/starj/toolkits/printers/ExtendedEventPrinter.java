package starj.toolkits.printers;

import java.io.PrintStream;

import starj.EventBox;
import starj.coffer.InvokeInstruction;
import starj.events.*;
import starj.toolkits.services.*;

public class ExtendedEventPrinter extends EventPrinter {
    public ExtendedEventPrinter(String name, String description) {
        super(name, description);
    }

    public ExtendedEventPrinter(String name, String description,
            PrintStream out) {
        super(name, description, out);
    }
    
    public void apply(EventBox box) {
        super.apply(box);
        Event event = box.getEvent();
        int id = event.getID();

        switch (id) {
            case Event.METHOD_ENTRY: {
                    MethodEntryEvent e = (MethodEntryEvent) event;
                    this.printMethod(e.getMethodID());
                    // Print call site
                    this.printCallSite(e.getCallSite());
                }
                break;
            case Event.METHOD_EXIT: {
                    MethodEvent e = (MethodEvent) event;
                    this.printMethod(e.getMethodID());
                }
                break;
            case Event.METHOD_ENTRY2: {
                    MethodEntry2Event e = (MethodEntry2Event) event;
                    this.printMethod(e.getMethodID());
                    this.printObject(e.getObjectID());
                    this.printCallSite(e.getCallSite());
                }
                break;
            case Event.OBJECT_ALLOC: {
                    ObjectAllocEvent e = (ObjectAllocEvent) event;
                    this.printObject(e.getObjectID());
                }
                break;
        }
    }

    private void printMethod(int id) {
        MethodEntity me = IDResolver.v().getMethodEntity(id);
        this.out.println("    Method => " + me);
    }
    
    private void printObject(int id) {
        ObjectEntity oe = IDResolver.v().getObjectEntity(id);
        this.out.println("    Object => " + oe);
    }
    
    private void printCallSite(InvokeInstruction call_site) {
        String s;
        if (call_site != null) {
            s = call_site.toString();
        } else {
            s = "N/A";
        }
        
        this.out.println("    Call site => " + s);
    }
}
