package starj.options;

import java.io.PrintStream;
import java.net.URL;
import java.util.*;

import starj.util.text.*;

public class ListHelpFormat implements HelpFormat {
    private static ColumnTextFormatter column_format
            = new ColumnTextFormatter(38, "    ", 39, "  ", "   ");
    private static TextWidthFormatter width_format
            = new TextWidthFormatter(80);

    private PrintStream out;
    private String program_name;
    private String main_class;
    private String version;
    private String description;
    private Author[] authors;
    private URL url;
    private String copyright;
    
    public ListHelpFormat(PrintStream out, String program_name, String version,
            String description, String main_class, Author[] authors, URL url,
            String copyright) {
        this.out = out;
        this.program_name = program_name;
        this.version = version;
        this.description = description;
        this.main_class = main_class;
        this.authors = authors;
        this.url = url;
        this.copyright = copyright;
    }

    public void formatHelp(Parser parser) {
        this.formatUsage(parser);
        this.out.println();

        ArgumentManager[] managers = parser.getManagers();
        Map category_map = new HashMap();
        for (int i = 0; i < managers.length; i++) {
            ArgumentManager manager = managers[i];
            if (manager instanceof OptionManager) {
                for (ArgumentIterator j = manager.iterator(); j.hasNext(); ) {
                    Option opt = (Option) j.next();
                    String opt_category = opt.getCategory();
                    List l;
                    if (category_map.containsKey(opt_category)) {
                        l = (List) category_map.get(opt_category);
                    } else {
                        l = new LinkedList();
                        category_map.put(opt_category, l);
                    }
                    l.add(opt);
                }
            }
        }

        for (Iterator i = category_map.keySet().iterator(); i.hasNext(); ) {
            String category = (String) i.next();
            this.out.println(category + " options:");
            List l = (List) category_map.get(category);
            for (Iterator j = l.iterator(); j.hasNext(); ) {
                Option opt = (Option) j.next();
                column_format.format("  " + this.formatOption(opt),
                        opt.getShortDescription(), this.out);
            }
            this.out.println();
        }
    }

    protected String formatOption(Option opt) {
        String arg_str = "";
        for (ArgumentIterator i = opt.iterator(); i.hasNext(); ) {
            arg_str += " " + this.formatArgument(i.next());
        }

        String opt_str = null;
        String[] short_switches = opt.getShortSwitches();
        Arrays.sort(short_switches, StringLengthComparator.v());
        for (int i = 0; i < short_switches.length; i++) {
            if (opt_str == null) {
                opt_str = "";
            } else {
                opt_str += ", ";
            }
            opt_str += OptionManager.makeShortSwitch(short_switches[i])
                    + arg_str;
        }

        String[] long_switches = opt.getLongSwitches();
        Arrays.sort(long_switches, StringLengthComparator.v());
        for (int i = 0; i < long_switches.length; i++) {
            if (opt_str == null) {
                opt_str = "";
            } else {
                opt_str += ", ";
            }
            opt_str += OptionManager.makeLongSwitch(long_switches[i])
                    + arg_str;
        }

        return opt_str;
    }
    
    public void formatUsage(Parser parser) {
        ArgumentManager[] managers = parser.getManagers();
        this.out.print("Usage: java " + this.main_class);
        for (int i = 0; i < managers.length; i++) {
            ArgumentManager manager = managers[i];
            if (manager instanceof DelimiterManager) {
                // Skip
            } else if (manager instanceof OptionManager) {
                this.out.print(" [options]");
            } else {
                for (ArgumentIterator j = manager.iterator(); j.hasNext(); ) {
                    this.out.print(" " + this.formatArgument(j.next()));
                }
            }
        }
        this.out.println();
        this.out.println(this.description);
    }

    private String formatArgument(Argument arg) {
        if (arg instanceof NamedFormattable) {
            String s = ((NamedFormattable) arg).getName();
            if (arg.isRequired()) {
                s = "<" + s + ">";
                if (arg.isRepeatable()) {
                    s += "+";
                }
            } else {
                s = "[" + s + "]";
                if (arg.isRepeatable()) {
                    s += "*";
                }
            }
            return s;
        } else {
            return "";
        }
    }

    protected String getVersionString() {
        return this.program_name + " version " + this.version;
    }

    public void formatVersion(Parser parser) {
        this.out.println(this.getVersionString());
        if (this.authors != null) {
            this.out.println("Written by:");
            for (int i = 0; i < this.authors.length; i++) {
                this.out.println("  " + this.authors[i]);
            }
        }
        if (this.url != null) {
            this.out.println("URL: " + this.url);
        }

        if (this.copyright != null) {
            this.out.println();
            if (this.authors != null) {
                this.out.print("Copyright (C) 2002-2004 ");
                for (int i = 0; i < this.authors.length; i++) {
                    if (i != 0) {
                        this.out.print(", ");
                    }
                    this.out.print(this.authors[i].getName());
                    
                }
                this.out.println("\n");
            }
            //this.out.println(this.copyright); // FIXME
            width_format.format(this.copyright).printTo(this.out);
        }
    }
}
