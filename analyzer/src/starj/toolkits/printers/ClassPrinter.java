package starj.toolkits.printers;

import java.io.PrintStream;

import starj.*;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;

public class ClassPrinter extends AbstractPrinter {
    public ClassPrinter(String name, String description) {
        super(name, description);
    }

    public ClassPrinter(String name, String description, PrintStream out) {
        super(name, description, out);
    }

    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = super.eventDependencies();
        
        dep_set.add(new EventDependency(Event.CLASS_LOAD,
                new TotalMask(Constants.FIELD_RECORDED),
                true));

        return dep_set;
    }
    
    public void apply(EventBox box) {
        ClassLoadEvent e = (ClassLoadEvent) box.getEvent();
        this.out.println("Class '" + e.getClassName() + "' loaded:");
        this.out.println("  id=" + e.getClassID());
        this.out.println("  source_name=" + e.getSourceName());
        this.out.println("  iface_count=" + e.getInterfaceCount());
        this.out.println("  " + e.getMethodCount() + " methods:");
        for (int i = 0; i < e.getMethodCount(); i++) {
            JVMPIMethod m = e.getMethod(i);
            this.out.println("    [" + m.getMethodID() + "]: "
                    + m.getMethodName() + m.getMethodSignature());
        }
    }
}
