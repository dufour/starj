package starj.options;

import java.util.*;

import starj.Application;

public class Parser {
    private StringQueue q;
    private StringQueue q_wrapper;
    private List managers;
    private ArgumentManager default_manager;
    private Set disabled_managers;
    private List post_processors;

    public Parser() {
        this.managers = new LinkedList();
        this.default_manager = new DefaultArgumentManager();
        this.post_processors = new LinkedList();
    }

    public void addManager(ArgumentManager manager) {
        if (!this.managers.contains(manager)) {
            this.managers.add(manager);
        }
    }

    public void addPostProcessor(PostProcessor processor) {
        this.post_processors.add(processor);
    }

    public void addArgument(Argument argument) {
        this.default_manager.addArgument(argument);
    }

    public ArgumentManager[] getManagers() {
        ArgumentManager[] managers = new ArgumentManager[this.managers.size()
                + 1];
        managers = (ArgumentManager[]) this.managers.toArray(managers);
        managers[managers.length - 1] = this.default_manager;
        return managers;
    }

    public void disable(ArgumentManager manager) {
        if (this.disabled_managers == null) {
            throw new RuntimeException("No parsing in progress");
        }

        this.disabled_managers.add(manager);
    }

    public void disableExtensionManagers() {
        for (Iterator i = this.managers.iterator(); i.hasNext(); ) {
            this.disable((ArgumentManager) i.next());
        }
    }

    public void enabled(ArgumentManager manager) {
        if (this.disabled_managers == null) {
            throw new RuntimeException("No parsing in progress");
        }

        this.disabled_managers.remove(manager);
    }

    public ParsedResult parse(String[] args) throws OptionProcessingException {
        this.disabled_managers = new HashSet();
        ParsedResult result = new ParsedResult();
        try {
            StringQueue q = new StringQueueImpl(args);
            this.q = q; // keep q local for fast access
            StringQueueWrapper q_wrapper = new StringQueueWrapper(q, this);
            this.q_wrapper = q_wrapper;

            ArgumentManager[] managers = this.getManagers();

            for (int i = 0; i < managers.length; i++) {
                managers[i].init();
            }

            while (!q.isEmpty()) {
                String next = q.peek();
                boolean parsed = false;
                for (int i = 0; i < managers.length; i++) {
                    ArgumentManager manager = managers[i];
                    if (!this.disabled_managers.contains(manager)
                            && manager.accept(next)) {
                        String tmp_fixme = q.get();
                        manager.parse(tmp_fixme, result, q_wrapper, this);
                        parsed = true;
                        break;
                    }
                }

                if (!parsed) {
                    //this.default_manager.parse(q.get(), result, q_wrapper,
                    //        this);
                    throw new OptionProcessingException("Unknown argument: '"
                            + next + "'");
                }
            }

            for (int i = 0; i < managers.length; i++) {
                managers[i].done();
            }

            for (int i = 0; i < managers.length; i++) {
                this.checkMissing(managers[i]);
            }
        } finally {
            this.disabled_managers = null;
        }

        for (Iterator i = this.post_processors.iterator(); i.hasNext(); ) {
            ((PostProcessor) i.next()).process(result);
        }

        //this.parsed_args = null;
        return result;
    }

    private void checkMissing(ArgumentManager manager) 
            throws OptionProcessingException {
        for (ArgumentIterator i = manager.iterator(); i.hasNext(); ) {
            Argument arg = i.next();
            if (manager.missing(arg)) {
                throw new OptionProcessingException(
                    "Missing required argument: " + arg);
            }
        }
    }

    void parseSet(ArgumentContainer container, ParsedResult result,
            StringQueue queue, boolean exhaustive)
            throws OptionProcessingException {
        Set parsed_args = new HashSet();
queue_loop:
        while (!queue.isEmpty()) {
            boolean parsed = false;
            String s = queue.peek();
            if (s == null) {
                break;
            }
            for (ArgumentIterator i = container.iterator(); i.hasNext(); ) {
                Argument arg = i.next();
                if (arg.isRepeatable() || !parsed_args.contains(arg)) {
                    if (arg.accept(s)) {
                        arg.parse(queue.get(), result, queue, this);
                        parsed_args.add(arg);
                        parsed = true;
                        break;
                    }
                }
            }

            if (!parsed) {
                if (exhaustive) {
                    throw new OptionProcessingException("Unknown argument: '"
                            + s + "'");
                } else {
                    break queue_loop;
                }
            }
        }

        for (ArgumentIterator i = container.iterator(); i.hasNext(); ) {
            Argument arg = i.next();
            if (arg.isRequired() && !parsed_args.contains(arg)) {
                throw new OptionProcessingException(
                        "Missing required argument: " + arg);
            }
        }

        if (exhaustive && !queue.isEmpty()) {
            throw new OptionProcessingException("Unknown argument: '" +
                    queue.peek() + "'");
        }
    }

    void parseList(ArgumentContainer container, ParsedResult result,
            StringQueue queue, boolean exhaustive)
            throws OptionProcessingException {
        Set parsed_args = new HashSet();
        
arg_loop:
        for (ArgumentIterator i = container.iterator(); i.hasNext(); ) {
            Argument arg = i.next();
            while (!queue.isEmpty()) {
                String s = queue.peek();
                if (arg.isRepeatable() || !parsed_args.contains(arg)) {
                    if (arg.accept(s)) {
                        arg.parse(queue.get(), result, queue, this);
                        parsed_args.add(arg);
                    } else if (arg.isRequired() && !parsed_args.contains(arg)) {
                        throw new OptionProcessingException(
                                "Missing required argument: " + arg);
                    } else {
                        continue arg_loop; // Try the next argument
                    }
                } else if (arg.isRequired() && !parsed_args.contains(arg)) {
                    throw new OptionProcessingException(
                            "Missing required argument: " + arg);
                } else {
                    continue arg_loop; // Try the next argument
                }
            }

            // Queue is empty, stop processing args
            break;
        }

        // Queue is empty, check for missing args
        for (ArgumentIterator i = container.iterator(); i.hasNext(); ) {
            Argument arg = i.next();
            if (arg.isRequired() && !parsed_args.contains(arg)) {
                throw new OptionProcessingException(
                        "Missing required argument: " + arg);
            }
        }

        if (exhaustive && !queue.isEmpty()) {
            throw new OptionProcessingException("Unknown argument: '" +
                    queue.peek() + "'");
        }
    }

    private class DefaultArgumentManager extends AbstractArgumentManager {
        public boolean accept(String arg) {
            return true;
        }
    }

    // FIXME: FOR TESTING ONLY
    public static void main(String[] args) throws OptionProcessingException {
        Parser parser = new Parser();

        // Delimiter support 
        parser.addManager(new DelimiterManager());

        // Option support
        ArgumentManager opt_manager = new OptionManager();
        parser.addManager(opt_manager);

        // Help option
        Option help_opt = new Option(0, false, true, "Category", "Displays help",
                "Displays help message");
        help_opt.addShortSwitch("h");
        help_opt.addLongSwitch("help");
        opt_manager.addArgument(help_opt);

        // Config option
        Option pack_opt = new Option(1, false, true, "Config",
                "Configures element", "Configures a hierarchy element");
        CSVArgument opt_patterns = new UnorderedCSVArgument(0, "Patterns",
                true, false, "Option patterns", "Option patterns",
                false);
        opt_patterns.addArgument(new IntArgument(3, "Number", false, true,
                "Number desc", "Number desc"));
        opt_patterns.addArgument(new StringArgument(3, "Pattern",
                    false,
                    true,
                    "Element pattern",
                    "Pattern which describes a set of elements"));
        pack_opt.addArgument(opt_patterns);
        pack_opt.addLongSwitch("config");
        opt_manager.addArgument(pack_opt);


        // String arg
        parser.addArgument(new StringArgument(0, "arg", true, true,
                    "Some argument", "Some argument that has no purpose"));

        // Arg collector
        parser.addPostProcessor(new ArgumentCollector());

        Author[] authors = new Author[1];
        authors[0] = new Author("Bruno Dufour", "bdufou1@sable.mcgill.ca",
                "Sable Research Group, McGill University");
        HelpFormat format = new ListHelpFormat(
                System.out,
                "*J",
                Application.VERSION_STRING,
                "A small test program for the Option Parser",
                "Parser",
                authors,
                null,
                "Copyright (C) 2004");
        System.out.println("//------------------//");
        format.formatUsage(parser);
        // System.out.println("//------------------//");
        // format.formatVersion(parser);
        System.out.println("//------------------//");
        format.formatHelp(parser);
        System.out.println("//------------------//");

        printResult(parser.parse(args), 0);
    }

    private static void printResult(ParsedResult result, int indent) {
        String indent_str = "";
        for (int i = 0; i < indent; i++) {
            indent_str += ' ';
        }
        System.out.println(indent_str + "+ " + result);
        if (result instanceof ParsedArgument) {
            ParsedArgument parg = (ParsedArgument) result;
            for (Iterator i = parg.values(); i.hasNext(); ) {
                System.err.println(indent_str + " \\-> " + i.next());
            }
        }
        ParsedResult[] children = result.getChildren();
        for (int i = 0; i < children.length; i++) {
            printResult(children[i], indent + 2);
        }
    }

    /** A StringQueue which prevents obtaining string arguments which
     * have been registered by user-defined ArgumentManagers.
     */
    private class StringQueueWrapper implements StringQueue {
        private StringQueue q;
        private Parser parser;

        public StringQueueWrapper(StringQueue q, Parser parser) {
            this.q = q;
            this.parser = parser;
        }

        private String getNextString(boolean consume) {
            for (Iterator i = this.parser.managers.iterator(); i.hasNext(); ) {
                ArgumentManager manager = (ArgumentManager) i.next();
                // Check if manager requests the string
                String next = this.q.peek();
                if (!this.parser.disabled_managers.contains(manager)
                        && manager.accept(next)) {
                    // Signal the requester that the request was denied
                    return null;
                }
            }

            if (consume) {
                return this.q.get();
            } else {
                return this.q.peek();
            }
        }

        public String get() {
            return this.getNextString(true);
        }

        public String peek() {
            return this.getNextString(false);
        }

        public String add(String arg) {
            return this.q.add(arg);
        }

        public String push(String arg) {
            return this.q.push(arg);
        }

        public int size() {
            int size = this.q.size();
            if (size <= 0) {
                return size;
            }
            if (this.peek() == null) {
                return 0;
            }
            return size;
        }

        public boolean isEmpty() {
            return (this.q.isEmpty() || this.peek() == null);
        }

        public void clear () {
            this.q.clear();
        }

        public String toString() {
            return this.q.toString();
        }
    }
}
