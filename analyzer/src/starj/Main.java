package starj;

import java.io.*;

import starj.dependencies.DependencyGraph;
import starj.io.PrintStreamWrapper;
import starj.io.logging.*;
import starj.io.traces.*;
import starj.options.*;
import starj.toolkits.metrics.*;
import starj.util.StringUtils;
import starj.util.text.*;

public class Main extends ClassPathApplication {
    public static final String DESC_STRING
            = "*J: A Tool for Dynamic Analysis of Java Programs";
    public static final int OPTION_HIERARCHY      =  0;
    public static final int OPTION_BUFFER_SIZE    =  1;
    public static final int OPTION_ELEMENT_CONFIG =  2;
    public static final int OPTION_XML_OUTPUT     =  3;
    public static final int OPTION_LIST_OUTPUT    =  4;
    public static final int OPTION_DOT_DEPS       =  5;
    public static final int OPTION_ELEMENT_HELP   =  6;

    public static final int ARG_FILENAME          =  0;
    
    private static ColumnTextFormatter column_format
            = new ColumnTextFormatter(20, "    ", 57, "  ", "   ");
    private static TextWidthFormatter width_format
            = new TextWidthFormatter(80);

    public Main(String[] args) {
        super(DESC_STRING, args);
    }

    public void main(ParsedResult[] args) {
        if (args == null || args.length < 1) {
            LogManager.v().logError("No trace file specified");
            System.exit(1);
        }
        String filename = (String) ((ParsedArgument) args[0]).getValue(0);
        try {
            InputStream stream
                    = InputStreamFactory.v().getInputStream(filename);
            TraceReader reader = TraceReaderFactory.v().getTraceReader(stream);
            Scene.v().performOperations(reader);
            LogManager.v().logMessage("Processing completed successfully",
                    Logger.IMPORTANT);
            return;
        } catch (ProcessingDeathException e) {
            LogManager.v().logError(e.getMessage());
        } catch (Exception e) {
            LogManager.v().logError("Unexpected error: " + e);
            e.printStackTrace();
        }
        LogManager.v().logError("Processing failed");
    }

    public static void main(String[] args) {
        Main main = new Main(args);
        main.run();
    }

    public void registerArgument(Argument arg) {
        //if (arg.getID() == Application.OPT_HELP) {
        //    ((Option) arg).addArgument(new ParameterAdapter(new StringArgument(
        //            0,
        //            "pack/op",
        //            false,
        //            false,
        //            "Pack or Operation name",
        //            "Fully qualified Pack or Operation name for which to "
        //            + "display specific help")
        //    ));
        //}

        super.registerArgument(arg);
    }

    public void registerArguments() {
        super.registerArguments();

        // 'Show hierarchy' option
        Option hierarchy_opt = new Option(
                OPTION_HIERARCHY, // ID
                "General",        // Category
                "Displays pack/operation hierarchy", // Short desc
                "Displays the pack/operation hierarchy as an ASCII tree"
        );
        hierarchy_opt.addShortSwitch("H");
        hierarchy_opt.addLongSwitch("show-hierarchy");
        this.registerArgument(hierarchy_opt);

        // Buffer size option
        Option buffer_size_opt = new Option(
                OPTION_BUFFER_SIZE,
                "IO",
                "Sets trace buffer",
                "Sets the buffer size used by the trace reader"
        );
        buffer_size_opt.addShortSwitch("b");
        buffer_size_opt.addLongSwitch("buffer-size");
        buffer_size_opt.addArgument(new IntArgument(
                0,                     // ID
                "size",                // name
                true,                  // required?
                false,                 // repeatable?
                "Size in bytes",       // short desc
                "Buffer size in bytes" // long desc
        ));
        this.registerArgument(buffer_size_opt);

        Option pack_opt = new ElementConfigOption(
                OPTION_ELEMENT_CONFIG,
                "General",
                "Sets pack/operation options",
                "Configures a pack or operation"
        );
        pack_opt.addShortSwitch("p");
        pack_opt.addLongSwitch("config");
        this.registerArgument(pack_opt);

        Option xml_opt = new Option(
                OPTION_XML_OUTPUT,
                "IO",
                "Defines a set of metric elements to collect XML output from",
                "Defines a set of metric elements to collect XML output from"
        );
        xml_opt.addArgument(new StreamNameArgument(
                0,
                "stream",
                true,  // required
                false, // not repeatable
                "Stream name",
                "Stream name (stdout, stderr, file:<filename> or filename)"
        ));
        xml_opt.addArgument(new ElementPatternsArgument(
                0,
                "patterns",
                true,  // required
                false, // not repeatable
                "Comma-separated name patterns",
                "Comma-separated name patterns specifying which elements to "
                + "collect metrics from",
                false  // not inlined
        ));
        xml_opt.addShortSwitch("x");
        xml_opt.addLongSwitch("xml");
        this.registerArgument(xml_opt);

        Option element_help_opt = new Option(
            OPTION_ELEMENT_HELP,
            "General",
            "Displays element-specific help",
            "Displays specific help for a list of elements"
        );
        element_help_opt.addArgument(new ElementPatternsArgument(
                0,
                "patterns",
                true,  // required
                false, // not repeatable
                "Comma-separated name patterns",
                "Comma-separated name patterns specifying which elements to "
                + "list help for",
                false  // not inlined
        ));
        element_help_opt.addLongSwitch("element-help");
        this.registerArgument(element_help_opt);

        Option lst_out_opt = new Option(
                OPTION_LIST_OUTPUT,
                "IO",
                "Defines a set of metric elements to collect text metric output from",
                "Defines a set of metric elements to collect text metric output from"
        );
        lst_out_opt.addArgument(new StreamNameArgument(
                0,
                "stream",
                true,  // required
                false, // not repeatable
                "Stream name",
                "Stream name (stdout, stderr, file:<filename> or filename)"
        ));
        lst_out_opt.addArgument(new ElementPatternsArgument(
                0,
                "patterns",
                true,  // required
                false, // not repeatable
                "Comma-separated name patterns",
                "Comma-separated name patterns specifying which elements to "
                + "collect metrics from",
                false  // not inlined
        ));
        lst_out_opt.addShortSwitch("m");
        lst_out_opt.addLongSwitch("text");
        this.registerArgument(lst_out_opt);
        
        // Dot dependency graph option
        Option dot_deps_opt = new Option(
                OPTION_DOT_DEPS,
                "IO",
                "Outputs dependency graph",
                "Outputs dependency graph in 'dot' format"
        );
        dot_deps_opt.addLongSwitch("dep-graph");
        dot_deps_opt.addArgument(new StringArgument(
                0,                        // ID
                "filename",               // name
                true,                     // required?
                false,                    // repeatable?
                "Graph filename",         // short desc
                "'Dot' graph output file" // long desc
        ));
        this.registerArgument(dot_deps_opt);

        // File name argument
        Argument trace_file = new FileNameArgument(
                ARG_FILENAME, // ID
                "trace file", // Display name
                false,        // Required
                false,        // Repeatable
                "Trace file", // Short desc
                "Trace file to be processed", //Long desc
                true          // Must exist
        );
        this.registerArgument(trace_file);
    }

    public void handleOption(ParsedOption parsed_option) {
        switch (parsed_option.getID()) {
            case OPTION_HIERARCHY:
                Scene.v().getRootPack().displayHierarchy(System.out);
                System.exit(0);
                break;
            case OPTION_BUFFER_SIZE: {
                    Integer value = (Integer) parsed_option.getValue(0, 0);
                    int buff_size = value.intValue();
                    InputStreamFactory.v().setBufferSize(buff_size);
                }
                break;
            case OPTION_ELEMENT_CONFIG: {
                    ParsedResult[] children = parsed_option.getChildren();
                    for (int i = 0; i < children.length; i++) {
                        ParsedElementConfig element_cfg
                                = (ParsedElementConfig) children[i];
                        HierarchyElement element = element_cfg.getElement();
                        ParsedResult[] configs = element_cfg.getChildren();
                        for (int j = 0; j < configs.length; j++) {
                            ParsedArgument arg = (ParsedArgument) configs[j];
                            element.configure(
                                    (ElementConfigArgument) arg.getArgument(),
                                    (arg.getChildCount() < 2 ?
                                            arg.getValue(0, 0)
                                            : arg.getValues(0))
                                    
                            );
                        }
                    }
                }
                break;
            case OPTION_XML_OUTPUT: {
                    PrintStreamWrapper wrapper
                            = (PrintStreamWrapper) parsed_option.getValue(0, 0);
                    try {
                        PrintStream stream = wrapper.getStream();
                        XMLMetricVisitor visitor = new XMLMetricVisitor(stream);
                        this.handleMetricOutputOption(parsed_option, visitor);
                    } catch (IOException e) {
                        throw new RuntimeException(e); // FIXME!!
                    }
                }
                break;
            case OPTION_LIST_OUTPUT: {
                    PrintStreamWrapper wrapper
                            = (PrintStreamWrapper) parsed_option.getValue(0, 0);
                    try {
                        PrintStream stream = wrapper.getStream();
                        TextMetricVisitor visitor
                                = new TextMetricVisitor(stream);
                        this.handleMetricOutputOption(parsed_option, visitor);
                    } catch (IOException e) {
                        throw new RuntimeException(e); // FIXME!!
                    }
                }
                break;
            case OPTION_DOT_DEPS: {
                    String filename = (String) parsed_option.getValue(0, 0);
                    try {
                        PrintStream out = new PrintStream(new FileOutputStream(
                            filename));
                        DependencyGraph g = new DependencyGraph(
                                Scene.v().getRootPack(), true);
                        g.toDot(out);
                    } catch (IOException e) {
                        throw new RuntimeException(e); // FIXME!!
                    }
                    System.exit(0);
                }
                break;
            case OPTION_ELEMENT_HELP: {
                    ElementSet elements = new ElementSet();
                    Object[] e = parsed_option.getValues(0);
                    for (int i = 0; i < e.length; i++) {
                        elements.add((HierarchyElement) e[i]);
                    }
                    
                    this.printElementHelp(elements);
                    System.exit(0);
                }
                break;
            default:
                super.handleOption(parsed_option);
        }
    }

    private void handleMetricOutputOption(ParsedOption opt,
            MetricVisitor visitor) {
        ElementSet elements = new ElementSet();
        Object[] ops = opt.getValues(1);
        for (int i = 0; i < ops.length; i++) {
            Object op = ops[i];
            if (op instanceof MetricOperation) {
                elements.add((MetricOperation) op);
            } else {
                LogManager.v().logWarning(op
                        + " is not a metric operation");
            }
        }

        Scene.v().addVisitor(new MetricOutputVisitor(visitor, elements));
    }

    private void printElementHelp(ElementSet elements) {
        PrintStream out = System.out;
        for (HierarchyElementIterator i = elements.iterator(); i.hasNext(); ) {
            this.printElementHelp(i.next(), out);
            out.println();
        }
    }

    private void printElementHelp(HierarchyElement e, PrintStream out) {
        String prefix;
        if (e instanceof Operation) {
            prefix = "Operation";
        } else if (e instanceof Pack) {
            prefix = "Pack";
        } else {
            prefix = "Element";
        }

        String s = prefix + " " + StringUtils.quote(e.getName()) + ":";
        out.println(s);
        out.println(StringUtils.stringOf('-', s.length()));
        out.println();
        String desc = e.getDescription();
        if (desc == null) {
            desc = "No description available.";
        }
        Main.width_format.format(StringUtils.indent(desc, 4), out);
        out.println();
        
        Main.column_format.format("Configuration", "Description", out);
        Main.column_format.format("-------------", "-----------", out);
        ElementConfigSet config_set = e.getConfigurationSet();
        ElementConfigArgument[] arguments = config_set.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            ElementConfigArgument arg = arguments[i];
            String name = arg.getName();
            Main.column_format.format(name, arg.getLongDescription(), out);
        }
    }
}
