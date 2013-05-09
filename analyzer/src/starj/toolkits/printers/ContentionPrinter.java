package starj.toolkits.printers;

import java.io.*;

import starj.*;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;
import starj.toolkits.services.*;

public class ContentionPrinter extends AbstractPrinter {
    public ContentionPrinter(String name, String description) {
        super(name, description);
    }
    
    public ContentionPrinter(String name, String description, PrintStream out) {
        super(name, description, out);
    }
    
    public OperationSet operationDependencies() {
        OperationSet ops = super.operationDependencies();
        ops.add(IDResolver.v());
        ops.add(CallStackManager.v());
        
        return ops;
    }
    
    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();
        
        dep_set.add(new EventDependency(Event.MONITOR_CONTENDED_ENTER,
                new TotalMask(Constants.FIELD_RECORDED),
                true));

        return dep_set;
    }

    public void apply(EventBox box) {
        MonitorEvent event = (MonitorEvent) box.getEvent();
        int obj_id = event.getObjectID();
        int env_id = event.getEnvID();
        this.out.println("CONTENTION");
        this.out.println("  object: " + IDResolver.v().getObjectEntity(obj_id)
                + " @ " + obj_id);
        this.out.println("  thread: " + IDResolver.v().getThreadEntity(env_id)
                + " @ " + env_id);
        this.out.println("  context: "
                + CallStackManager.v().getCurrentMethodEntity(env_id));
    }
}
