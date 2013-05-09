package starj.dependencies;

import java.util.*;
import starj.*;

public class OperationSet {
    private Set set;
    
    public OperationSet() {
        this.clear();
    }

    public boolean add(Operation operation) {
        return this.set.add(operation);
    }

    public void clear() {
        this.set = new HashSet();
    }

    public boolean contains(Operation operation) {
        return this.set.contains(operation);
    }

    public boolean remove(Operation operation) {
        return this.set.remove(operation);
    }

    public OperationIterator iterator() {
        return new OperationSetIterator();
    }

    public int size() {
        return this.set.size();
    }

    private class OperationSetIterator implements OperationIterator {
        private Iterator iterator;

        OperationSetIterator() {
            this.iterator = OperationSet.this.set.iterator();
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public Operation next() {
            return (Operation) this.iterator.next();
        }
    }
}
