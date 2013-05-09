package starj.options;

import java.util.*;

public class ParsedResult {
    private List children;

    public ParsedResult() {
        this.children = new LinkedList();
    }

    public void addChild(ParsedResult parsed_arg) {
        this.children.add(parsed_arg);
    }

    public ParsedResult getChild(int index) {
        return (ParsedResult) this.children.get(index);
    }

    public int getChildCount() {
        return this.children.size();
    }

    public ParsedResult[] getChildren() {
        ParsedResult[] children_array = new ParsedResult[this.children.size()];
        return (ParsedResult[]) this.children.toArray(children_array);
    }

    public void sort(Comparator comparator) {
        Collections.sort(this.children, comparator);
    }
}
