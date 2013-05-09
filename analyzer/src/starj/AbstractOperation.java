package starj;

import starj.dependencies.OperationSet;
import starj.dependencies.EventDependencySet;

public abstract class AbstractOperation extends AbstractHierarchyElement
        implements Operation {
	
    public AbstractOperation(String name, String description) {
        super(name, description);
    }

    public OperationSet operationDependencies() {
        return new OperationSet();
    }

    public EventDependencySet eventDependencies() {
        return new EventDependencySet();
    }
    
    public void init() {
        // Intentionally empty
    }

    public abstract void apply(EventBox box);
    
    public void done() {
        // Intentionally empty
    }
}
