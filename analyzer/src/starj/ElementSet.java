package starj;

import java.util.*;

public class ElementSet {
    private Set set;
    
    public ElementSet() {
        this(null);
    }

    public ElementSet(HierarchyElement[] elements) {
        this.clear();
        if (elements != null) {
            for (int i = 0; i < elements.length; i++) {
                this.add(elements[i]);
            }
        }
    }

    public boolean add(HierarchyElement element) {
        return this.set.add(element);
    }

    public void clear() {
        this.set = new HashSet();
    }

    public boolean contains(HierarchyElement element) {
        return this.set.contains(element);
    }

    public boolean remove(HierarchyElement element) {
        return this.set.remove(element);
    }

    public HierarchyElementIterator iterator() {
        return new ElementSetIterator();
    }

    public int size() {
        return this.set.size();
    }

    private class ElementSetIterator implements HierarchyElementIterator {
        private Iterator iterator;

        ElementSetIterator() {
            this.iterator = ElementSet.this.set.iterator();
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public HierarchyElement next() {
            return (HierarchyElement) this.iterator.next();
        }
    }
}
