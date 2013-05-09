package starj.util.text;

import java.util.*;
import java.io.PrintStream;

public class Strings {
    List strings;
    private int pos;

    public Strings() {
        this.pos = 0;
        this.strings = new ArrayList();
    }

    public Strings(String[] strings) {
        this();
        this.strings.addAll(Arrays.asList(strings));
    }

    public Strings(Strings strings) {
        this();
        this.addAll(strings);
    }

    public void add(String s) {
        this.strings.add(s);
    }

    public void addAll(Strings strings) {
        this.strings.addAll(strings.strings);
    }

    public StringsIterator iterator() {
        return new StringsIterator(this);
    }

    public String toString() {
        String rv = "";
        for (Iterator i = this.strings.iterator(); i.hasNext(); ) {
            rv += i.next() + (i.hasNext() ? "\n" : "");
        }
        
        return rv;
    }

    public int size() {
        return this.strings.size();
    }

    public void printTo(PrintStream out) {
        for (Iterator i = this.strings.iterator(); i.hasNext(); ) {
            out.println(i.next());
        }
    }
}
