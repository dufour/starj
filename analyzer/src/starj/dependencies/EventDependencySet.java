package starj.dependencies;

import java.util.*;

public class EventDependencySet {
    private Set set;
    
    public EventDependencySet() {
        this.clear();
    }

    public boolean add(EventDependency dependency) {
        return this.set.add(dependency);
    }

    public void clear() {
        this.set = new HashSet();
    }

    public boolean contains(EventDependency dependency) {
        return this.set.contains(dependency);
    }

    public boolean remove(EventDependency dependency) {
        return this.set.remove(dependency);
    }

    public EventDependencyIterator iterator() {
        return new EventDependencySetIterator();
    }

    public int size() {
        return this.set.size();
    }

    private class EventDependencySetIterator
            implements EventDependencyIterator {
        private Iterator iterator;

        EventDependencySetIterator() {
            this.iterator = EventDependencySet.this.set.iterator();
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public EventDependency next() {
            return (EventDependency) this.iterator.next();
        }
    }
}
