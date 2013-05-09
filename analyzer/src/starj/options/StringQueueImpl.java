package starj.options;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Iterator;

public class StringQueueImpl implements StringQueue {
    private LinkedList contents;
    private int mod_count = 0;

    public StringQueueImpl() {
        this.contents = new LinkedList();
    }

    public StringQueueImpl(String arg) {
        this();
        this.add(arg);
    }

    public StringQueueImpl(String[] args) {
        if (args != null) {
            this.contents = new LinkedList(Arrays.asList(args));
        } else {
            this.contents = new LinkedList();
        }
    }
    
    public String add(String arg) {
        this.mod_count++;
        this.contents.add(arg);
        return arg;
    }

    public String get() {
        return (String) this.contents.removeFirst();
    }

    public String push(String arg) {
        this.mod_count++;
        this.contents.addFirst(arg);
        return arg;
    }

    public String peek() {
        return (String) this.contents.getFirst();
    }
    
    public int size() {
        return this.contents.size();
    }

    public boolean isEmpty() {
        return (this.contents.size() == 0);
    }

    public void clear () {
        this.mod_count++;
        this.contents.clear();
    }

    public String toString() {
        String rv = "{";
        for (Iterator i = this.contents.iterator(); i.hasNext(); ) {
            rv += i.next();
            if (i.hasNext()) {
                rv += ", ";
            }
        }
        rv += "}";
        return rv;
    }
}
