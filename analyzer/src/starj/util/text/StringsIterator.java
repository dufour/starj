package starj.util.text;

import java.util.ListIterator;

public class StringsIterator {
    private ListIterator iterator;
    
    StringsIterator(Strings strings) {
        this.iterator = strings.strings.listIterator();
    }

    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    public String next() {
        return (String) this.iterator.next();
    }

    public void pushBack() {
        if (this.iterator.hasPrevious()) {
            this.iterator.previous();
        }
    }
}
