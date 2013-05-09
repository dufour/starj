package starj.options;

import java.util.*;

public class Option extends FormattableArgument
        implements MutableArgumentContainer {
    private Set short_switches;
    private Set long_switches;
    private List arguments;
    private String category;
    
    public Option(int id, String category, String short_desc, String long_desc) {
        this(id, false, true, category, short_desc, long_desc);
    }

    public Option(int id, boolean required, String category,
            String short_desc, String long_desc) {
        this(id, required, true, category, short_desc, long_desc);
    }

    public Option(int id, boolean required, boolean repeatable,
            String category, String short_desc, String long_desc) {
        super(id, required, repeatable, short_desc, long_desc);
        this.category = category;
        this.short_switches = new HashSet();
        this.long_switches = new HashSet();
        this.arguments = new LinkedList();
    }

    private boolean addSwitch(String sw, Set set) {
        if (sw.length() < 1) {
            throw new RuntimeException("Invalid switch: '" + sw + "'");
        }
        boolean rv = set.add(sw);

        return rv;
    }

    public boolean addShortSwitch(String sw) {
        return this.addSwitch(sw, this.short_switches);
    }

    public boolean addLongSwitch(String sw) {
        return this.addSwitch(sw, this.long_switches);
    }

    public void addArgument(Argument argument) {
        this.arguments.add(argument);
    }

    public int getArgumentCount() {
        return this.arguments.size();
    }

    public ArgumentIterator iterator() {
        return new OptionArgumentIterator(this);
    }

    public String getCategory() {
        return this.category;
    }
    
    public boolean accept(String arg) {
        return true; // The OptionManager class will handle this
    }

    public void parse(String arg, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException {
        ParsedArgument parsed_arg = new ParsedOption(this);
        result.addChild(parsed_arg);
        parser.parseList(this, parsed_arg, queue, false);
    }

    public String[] getShortSwitches() {
        String[] switches = new String[this.short_switches.size()];
        this.short_switches.toArray(switches);
        return switches;
    }

    public String[] getLongSwitches() {
        String[] switches = new String[this.long_switches.size()];
        this.long_switches.toArray(switches);
        return switches;
    }

    private class OptionArgumentIterator implements ArgumentIterator {
        private Iterator iterator;
        
        OptionArgumentIterator(Option option) {
            this.iterator = option.arguments.iterator();
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public Argument next() {
            return (Argument) this.iterator.next();
        }
    }
}
