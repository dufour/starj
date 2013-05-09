package starj.options;

import java.util.*;

public class ElementConfigArgument extends NamedArgument
        implements MutableArgumentContainer {
    public static final String SEPARATOR = ":";

    private List arguments;
    private boolean ordered;

    public ElementConfigArgument(int id, String name, String short_desc,
            String long_desc, boolean ordered) {
        this(id, name, false, false, short_desc, long_desc, ordered);
    }

    public ElementConfigArgument(int id, String name, boolean required,
            String short_desc, String long_desc, boolean ordered) {
        this(id, name, required, false, short_desc, long_desc, ordered);
    }

    public ElementConfigArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc,
            boolean ordered) {
        super(id, name, required, repeatable, short_desc, long_desc);
        this.arguments = new LinkedList();
        this.ordered = ordered;
    }

    public ArgumentIterator iterator() {
        return new ElementConfigArgumentIterator(this);
    }
    
    public int getArgumentCount() {
        return this.arguments.size();
    }

    public void addArgument(Argument argument) {
        this.arguments.add(argument);
    }

    public boolean isOrdered() {
        return this.ordered;
    }

    protected void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public boolean accept(String argument) {
        String name = this.getName();
        return argument.equals(name) || argument.startsWith(name + SEPARATOR);
    }

    public void parse(String arg, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException {
        String[] s = arg.split(SEPARATOR, 2);
        
        StringQueue q;
        if (s.length < 2) {
            q = new StringQueueImpl("true"); // TODO: make more generic
        } else {
            q = new StringQueueImpl(s[1]);
        }
        
        ParsedArgument parsed_arg = new ParsedArgument(this);
        result.addChild(parsed_arg);

        if (this.ordered) {
            parser.parseList(this, parsed_arg, q, true);
        } else {
            parser.parseSet(this, parsed_arg, q, true);
        }
    }

    private class ElementConfigArgumentIterator implements ArgumentIterator {
        private Iterator iterator;
        
        ElementConfigArgumentIterator(ElementConfigArgument arg) {
            this.iterator = arg.arguments.iterator();
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public Argument next() {
            return (Argument) this.iterator.next();
        }
    }
}
