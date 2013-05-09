package starj.options;

import java.util.*;

public abstract class AbstractArgumentManager implements ArgumentManager {
    private List contents;
    private Set parsed_args;
    
    public AbstractArgumentManager() {
        this.contents = new LinkedList();
    }

    public void addArgument(Argument argument) {
        if (!this.contents.contains(argument)) {
            this.contents.add(argument);
        }
    }

    public void init() {
        this.parsed_args = new HashSet();
    }

    public void done() {
        // Intentionally empty
    }
    
    public ArgumentIterator iterator() {
        return new ArgumentManagerIterator(this);
    }

    public int getArgumentCount() {
        return this.contents.size();
    }
    
    public abstract boolean accept(String argument);
    public void parse(String argument, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException {
        boolean parsed = false;
        for (ArgumentIterator i = this.iterator(); i.hasNext(); ) {
                Argument candidate = i.next();
                if (this.enabled(candidate)) {
                    if (candidate.accept(argument)) {
                        this.parse(candidate, argument, result, queue, parser);
                        parsed = true;
                        break;
                    }
                }
        }

        if (!parsed) {
            throw new OptionProcessingException("Unknown argument: '"
                    + argument + "'");
        }
    }

    public void parse(Argument arg, String arg_str, ParsedResult result, 
            StringQueue queue, Parser parser) throws OptionProcessingException {
        if (!this.enabled(arg)) {
            throw new OptionProcessingException("Unknown argument: " + arg_str);
        }
        arg.parse(arg_str, result, queue, parser);
        this.parsed_args.add(arg);
    }

    public boolean enabled(Argument arg) {
        return (arg.isRepeatable() || !this.parsed_args.contains(arg));
    }

    public boolean missing(Argument arg) {
        return arg.isRequired() && !this.parsed_args.contains(arg);
    }

    private class ArgumentManagerIterator implements ArgumentIterator {
        private Iterator iterator;
        
        ArgumentManagerIterator(AbstractArgumentManager manager) {
            this.iterator = manager.contents.iterator();
        }

        public Argument next() {
            return (Argument) this.iterator.next();
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }
    }
}
