package starj;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.Iterator; // FIXME: temporary


import starj.options.*;
import starj.io.logging.LogManager;

/**
 * Class representing a *J console application.
 */
public abstract class Application implements Runnable {
    public static final String LGPL_TEXT = 
            "This library is free software; you can redistribute it and/or "
            + "modify it under the terms of the GNU Lesser General Public "
            + "License as published by the Free Software Foundation; either "
            + "version 2.1 of the License, or (at your option) any later "
            + "version.\n\n"
            + "This library is distributed in the hope that it will be useful, "
            + "but WITHOUT ANY WARRANTY; without even the implied warranty of "
            + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the "
            + "GNU Lesser General Public License for more details.\n\n"
            + "You should have received a copy of the GNU Lesser General "
            + "Public License along with this library; if not, write to the "
            + "Free Software Foundation, Inc., 59 Temple Place, Suite 330, "
            + "Boston, MA  02111-1307  USA.";

    public static final String NAME_STRING = "*J";
    public static final String VERSION_STRING = "0.1-beta8";
    public static final Author[] AUTHORS = {
            new Author("Bruno Dufour", "bruno.dufour@mail.mcgill.ca")
    };

    // Initialize URL
    public static final URL STARJ_URL;
    static {
        URL url = null;
        try {
            url = new URL("http://www.sable.mcgill.ca/starj/");
        } catch (MalformedURLException e) {
            url = null;
        }
        STARJ_URL = url;
    }
    
    private String description;
    private String[] args;
    private StandardOptionParser option_parser;
    private HelpFormat help_format;

    // Option IDs for parsing. Use negative numbers here to avoid conflicts
    // in subclasses
    public static final int OPT_HELP         = -1;
    public static final int OPT_VERSION      = -2;
    public static final int OPT_LOG          = -3;

    public Application(String description, String[] args) {
        this.description = description;
        this.args = args;
        this.help_format = new ListHelpFormat(System.out,
                NAME_STRING, VERSION_STRING, this.description,
                this.getClass().getName(), AUTHORS, STARJ_URL, LGPL_TEXT);
    }

    public abstract void main(ParsedResult[] args);

    private void printResult(ParsedResult result, int indent) {
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
    
    public void run() {
        this.option_parser = new StandardOptionParser();
    
        // Register other arguments
        this.registerArguments();
        ParsedResult result = null;
        try {
            result = this.option_parser.parse(this.args);
            //this.printResult(result, 0); // FOR DEBUGGING ONLY
        } catch (OptionProcessingException e) {
            LogManager.v().logError("Option processing failed:");
            LogManager.v().logError("  " + e.getMessage());
            System.exit(1);
        }

        ParsedResult main_args = new ParsedResult();
        if (result != null) {
            // Find all value args (they will be at the end because
            // we added a post-processor to the parser)
            ParsedResult[] parsed_args = result.getChildren();
            for (int i = 0; i < parsed_args.length; i++) {
                ParsedResult r = parsed_args[i];
                if (r instanceof ParsedOption) {
                    this.handleOption((ParsedOption) r);
                } else {
                    main_args.addChild(r);
                }
            }
        }

        this.main(main_args.getChildren());
    }

    public void registerArgument(Argument arg) {
        this.option_parser.addArgument(arg);
    }

    public void registerArguments() {
        // Help option
        Option help_option = new Option(
                OPT_HELP,
                "General",
                "Print helps and exits",
                "Prints a textual help message and exits"
        );
        help_option.addShortSwitch("h");
        help_option.addLongSwitch("help");
        this.registerArgument(help_option);

        // Version option
        Option version_option = new Option(
                OPT_VERSION,
                "General",
                "Prints version and exits",
                "Prints the version information for this application and exits"
        );
        version_option.addShortSwitch("v");
        version_option.addLongSwitch("version");
        this.registerArgument(version_option);
    }

    public void printUsage() {
        this.help_format.formatUsage(this.option_parser);
    }

    public void printHelp() {
        this.help_format.formatHelp(this.option_parser);
    }

    public void printVersion() {
        this.help_format.formatVersion(this.option_parser);
    }

    public void handleOption(ParsedOption parsed_option) {
        switch (parsed_option.getID()) {
            case OPT_HELP:
                this.printHelp();
                System.exit(0);
                break;
            case OPT_VERSION:
                this.printVersion();
                System.exit(0);
                break;
            default:
                throw new RuntimeException("Unsupported option ID: " +
                        parsed_option.getID());
        }
    }
}
