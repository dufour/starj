package starj;

import java.util.*;
import java.util.regex.*;

public class Pack extends AbstractHierarchyElement implements Container {
    private LinkedHashMap contents;

    public Pack(String name, String description) {
        super(name, description);
        this.reset();
    }

    public void add(HierarchyElement element) {
        String name = element.getName();
        if (name.indexOf('.') >= 0) {
            throw new RuntimeException("Invalid element name: '" + name + "'");
        }
        if (this.contents.containsKey(name)) {
            throw new RuntimeException("Duplicate name: '" + name + "'");
        }
        if (name.indexOf('.') >= 0) {
            throw new RuntimeException("Invalid name: '" + name + "'");
        }
        this.contents.put(name, element);
    }

    public HierarchyElement getByName(String name) {
        int dot_index = name.indexOf('.');
        String element_name;
        if (dot_index >= 0) {
            element_name = name.substring(0, dot_index);
        } else {
            element_name = name;
        }
        HierarchyElement element
                = (HierarchyElement) this.contents.get(element_name);
        if (element != null && dot_index >= 0) {
            if (!(element instanceof Container)) {
                throw new RuntimeException("'" + element_name
                        + "' is not a container");
            }

            Container container = (Container) element;
            return container.getByName(name.substring(dot_index + 1));
        }

        return element;
    }

    /**
     * Returns an array of all {@link HierarchyElement HierarchyElements}
     * which match the given pattern. Valid pattern constructs are:
     *
     * <ul>
     *   <li><code>*</code>: Matches any string for one hierarchy level</li>
     *   <li><code>**</code>: Matches any string accross hierarchy levels</li>
     *   <li><code>?</code>: Matches any single character</li>
     * <ul>
     */
    public HierarchyElement[] findElements(String pattern) {
        NamePatternMatcher matcher = new NamePatternMatcher(pattern);
        NameMatcherVisitor visitor = new NameMatcherVisitor(matcher);
        visitor.visit(this);
        return visitor.getMatchingElements();

    }

    public HierarchyElementIterator iterator() {
        return new PackIterator();
    }

    public void reset() {
        this.contents = new LinkedHashMap();
    }

    private class PackIterator implements HierarchyElementIterator {
        private Iterator iterator;

        PackIterator() {
            this.iterator = Pack.this.contents.values().iterator();
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public HierarchyElement next() {
            return (HierarchyElement) this.iterator.next();
        }
    }

    private class NameMatcherVisitor extends ElementNameVisitor {
        private ElementNameMatcher matcher;
        private List matches;
        
        NameMatcherVisitor(ElementNameMatcher matcher) {
            super(true);
            this.matcher = matcher;
            this.matches = new LinkedList();
        }

        public void visitElement(HierarchyElement element, String name) {
            if (this.matcher.matches(name)) {
                this.matches.add(element);
            }
        }

        public HierarchyElement[] getMatchingElements() {
            HierarchyElement[] rv = new HierarchyElement[this.matches.size()];
            this.matches.toArray(rv);
            return rv;
        }
    }

    private class NamePatternMatcher implements ElementNameMatcher {
        private Pattern pattern;

        NamePatternMatcher(String pattern) {
            pattern = pattern.replaceAll("\\.", "\\\\.");
            Pattern p = Pattern.compile("(\\*)+");
            Matcher m = p.matcher(pattern);
            String re = "^";
            int pos = 0;
            while (m.find()) {
                String wildcard = m.group();
                int start = m.start();
                int end = m.end();

                if (start > pos) {
                    re += pattern.substring(pos, start);
                }
                if (wildcard.length() > 1) {
                    re += ".*";
                } else {
                    re += "[^.]*";
                }
                pos = end;
            }

            if (pos < pattern.length()) {
                re += pattern.substring(pos);
            }
            re += "$";

            this.pattern = Pattern.compile(re);
        }

        public boolean matches(String name) {
            return this.pattern.matcher(name).matches();
        }
    }
}
