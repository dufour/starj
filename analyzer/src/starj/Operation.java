package starj;

import starj.dependencies.OperationSet;
import starj.dependencies.EventDependencySet;

/**
 * <code>Operation</code> must be implemented by all classes which
 * may appear in the event pipe.
 */
public interface Operation extends HierarchyElement {
    public void init();
    public OperationSet operationDependencies();
    public EventDependencySet eventDependencies();
    public void apply(EventBox box);
    public void done();
}
