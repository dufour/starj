package starj.options;

import java.util.*;

public abstract class CSVArgument extends NamedArgument
        implements MutableArgumentContainer, Inlinable {
    private List arguments;
    private boolean ordered;
    private boolean inline; // Should we parse everything into the existing
                            // result object (as opposed to creating a new one)?
    public CSVArgument(int id, String name, String short_desc,
            String long_desc, boolean ordered, boolean inline) {
        super(id, name, short_desc, long_desc);
        this.init(ordered, inline);
    }

    public CSVArgument(int id, String name, boolean required, 
            String short_desc, String long_desc, boolean ordered,
            boolean inline) {
        super(id, name, required, short_desc, long_desc);
        this.init(ordered, inline);
    }

    public CSVArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc,
            boolean ordered, boolean inline) {
        super(id, name, required, repeatable, short_desc, long_desc);
        this.init(ordered, inline);
    }

    private void init(boolean ordered, boolean inline) {
        this.arguments = new LinkedList();
        this.ordered = ordered;
        this.inline = inline;
    }

    public boolean accept(String arg) {
        return true;
    }

    public boolean isInlined() {
        return this.inline;
    }

    protected void setInlined(boolean inline) {
        this.inline = inline;
    }

    public boolean isOrdered() {
        return this.ordered;
    }

    protected void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public ArgumentIterator iterator() {
        return new CSVArgumentIterator(this);
    }
    
    public int getArgumentCount() {
        return this.arguments.size();
    }

    public void addArgument(Argument argument) {
        this.arguments.add(argument);
    }

    public void parse(String arg, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException {
        if (!this.inline) {
            ParsedArgument parsed_arg = new ParsedArgument(this);
            result.addChild(parsed_arg);
            result = parsed_arg;
        }
        StringQueue q = new StringQueueImpl(arg.split(","));
        if (this.ordered) {
            parser.parseList(this, result, q, true);
        } else {
            parser.parseSet(this, result, q, true);
        }
    }

    private class CSVArgumentIterator implements ArgumentIterator {
        private Iterator iterator;
        
        CSVArgumentIterator(CSVArgument csv_arg) {
            this.iterator = csv_arg.arguments.iterator();
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public Argument next() {
            return (Argument) this.iterator.next();
        }
    }
}
