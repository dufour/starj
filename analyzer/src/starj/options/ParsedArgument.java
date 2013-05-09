package starj.options;

import java.util.*;

public class ParsedArgument extends ParsedResult {
    private Argument argument;
    private List values;

    public ParsedArgument(Argument argument) {
        super();
        this.argument = argument;
        this.values = new LinkedList();
    }

    public ParsedArgument(Argument argument, Object value) {
        this(argument);
        this.values.add(value);
    }
    
    public int getID() {
        return this.argument.getID();
    }

    public Argument getArgument() {
        return this.argument;
    }

    public void addValue(Object value) {
        this.values.add(value);
    }
    
    public Object getValue(int index) {
        return this.values.get(index);
    }

    public int getValueCount() {
        return this.values.size();
    }

    public Object[] getValues() {
        return this.values.toArray();
    }

    public Iterator values() {
        return this.values.iterator();
    }

    public String toString() {
        return this.getClass().getName() + ": " + this.argument;
    }

    public Object getValue(int child_index, int value_index) {
        return ((ParsedArgument) this.getChild(child_index)).getValue(
                value_index);
    }

    public Object[] getValues(int child_index) {
        return ((ParsedArgument) this.getChild(child_index)).getValues();
    }

    public Iterator values(int child_index) {
        return ((ParsedArgument) this.getChild(child_index)).values();
    }
}
