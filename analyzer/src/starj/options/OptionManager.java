package starj.options;

import java.util.*;
import java.util.regex.*;

import starj.util.text.ReverseStringLengthComparator;

// FIXME: add caching

public class OptionManager extends AbstractArgumentManager {
    public static final Pattern SHORT_PATTERN
            = Pattern.compile(makeShortRegex("[^-].*"));
    public static final Pattern LONG_PATTERN
            = Pattern.compile(makeLongRegex(".+"));
    private static final Comparator comparator
            = new ReverseStringLengthComparator();

    private Map short_switch_map;
    private Map long_switch_map;
    
    public OptionManager() {
        this.short_switch_map = new HashMap();
        this.long_switch_map = new HashMap();
    }

    public void addArgument(Argument argument) {
        if (argument instanceof Option) {
            this.addOption((Option) argument);
        } else {
            throw new RuntimeException("Argument is not an option: "
                    + argument);
        }
    }

    public void addOption(Option option) {
        String[] short_switches = option.getShortSwitches();
        String[] long_switches = option.getLongSwitches();

        if (short_switches != null) {
            for (int i = 0; i < short_switches.length; i++) {
                String sw = short_switches[i];
                if (this.short_switch_map.containsKey(sw)) {
                    // Switch clash!
                    throw new RuntimeException("Short switch \'" + sw
                            + "\' already defined");
                }

                this.short_switch_map.put(sw, option);
            }
        }

        if (long_switches != null) {
            for (int i = 0; i < long_switches.length; i++) {
                String sw = long_switches[i];
                if (this.long_switch_map.containsKey(sw)) {
                    // Switch clash!
                    throw new RuntimeException("Long switch \'" + sw
                            + "\' already defined");
                }

                this.long_switch_map.put(sw, option);
            }
        }
    }

    public ArgumentIterator iterator() {
        return new OptionManagerIterator(this);
    }

    public int getArgumentCount() {
        Set s = new HashSet(this.short_switch_map.values());
        s.addAll(this.long_switch_map.values());
        return s.size();
    }

    public boolean accept(String arg) {
        return SHORT_PATTERN.matcher(arg).matches()
                || LONG_PATTERN.matcher(arg).matches();
    }

    public void parse(String argument, ParsedResult result, StringQueue queue,
            Parser parser) throws OptionProcessingException {
        Matcher matcher = LONG_PATTERN.matcher(argument);
        if (matcher.matches()) {
            // Long option found
            String arg_body = matcher.group(1);
            Matcher m = this.buildLongPattern().matcher(arg_body);
            int wildcard_group = m.groupCount();

            if (!m.matches()) {
                // Should not happen, by construction of regexp
                throw new OptionProcessingException("Invalid option: "
                        + argument);
            }

            String opt_switch = m.group(2);
            String opt_param = m.group(wildcard_group);
            if (opt_param != null && opt_param.length() > 0) {
                throw new OptionProcessingException(
                        "Parameters not supported: '" + opt_param + "'");
            }

            Option opt = (Option) this.long_switch_map.get(opt_switch);
            if (!this.enabled(opt)) {
                throw new OptionProcessingException("Duplicate option: "
                        + argument);
            }
            this.parse(opt, opt_switch, result, queue, parser);
        } else {
            matcher = SHORT_PATTERN.matcher(argument);
            if (matcher.matches()) {
                String arg_body = matcher.group(1);

                Matcher m = this.buildShortPattern().matcher(arg_body);
                int wildcard_group = m.groupCount();

                while (m.find()) {
                    if (m.group(wildcard_group) != null) {
                        // This is an invalid match, since non-switch arguments
                        // must be handled when processing the associated option
                        // switch
                        throw new OptionProcessingException("Invalid option: "
                                + m.group(wildcard_group));
                    }
                    
                    String opt_switch = m.group(1);
                    Option opt = (Option) this.short_switch_map.get(opt_switch);
                    this.parse(opt, opt_switch, result, queue, parser);
                }
            } else {
                throw new OptionProcessingException("Unknown option: "
                        + argument);
            }
        }
    }

    private Pattern buildLongPattern() {
        List l = new ArrayList(this.long_switch_map.keySet());
        // Sort the switches by decreasing length
        Collections.sort(l, OptionManager.comparator);
        String re = "^((";
        for (Iterator i = l.iterator(); i.hasNext(); ) {
            String sw = (String) i.next();
            re += "(" + sw + ")";
            if (i.hasNext()) {
                re += "|";
            }
        }
        re += ")(.*))$";
        return Pattern.compile(re);
    }

    private Pattern buildShortPattern() {
        List l = new ArrayList(this.short_switch_map.keySet());
        // Sort the switches by decreasing length
        Collections.sort(l, OptionManager.comparator);
        String re = "(";
        for (Iterator i = l.iterator(); i.hasNext(); ) {
            String sw = (String) i.next();
            re += "(" + sw + ")|";
        }
        re += "(.+))"; // Wildcard group has to appear last

        return Pattern.compile(re);
    }

    public static String makeShortSwitch(String sw) {
        return "-" + sw;
    }

    public static String makeLongSwitch(String sw) {
        return "--" + sw;
    }

    private static String makeShortRegex(String sw) {
        return "^-(" + sw + ")";
    }

    private static String makeLongRegex(String sw) {
        return "^--(" + sw + ")";
    }

    private class OptionManagerIterator implements ArgumentIterator {
        private Iterator iterator;
        public OptionManagerIterator(OptionManager manager) {
            Set s = new HashSet(manager.short_switch_map.values());
            s.addAll(manager.long_switch_map.values());
            this.iterator = s.iterator();
        }

        public Argument next() {
            return (Argument) this.iterator.next();
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }
    }
}
