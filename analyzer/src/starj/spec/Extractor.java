package starj.spec;

import java.io.*;

import starj.Application;
import starj.io.logging.LogManager;
import starj.io.traces.TraceIOException;
import starj.options.*;

public class Extractor extends Application {
    public static final String DESC_STRING = "*J Event specification extractor";
    private static final int ARG_INFILE   = 0;
    private static final int ARG_OUTFILE  = 1;
    
    private PrintStream out;

    public Extractor(String[] args) {
        super(Extractor.DESC_STRING, args);
    }

    public void main(ParsedResult[] args) {
        if (args.length < 1) {
            this.printUsage();
            System.exit(0);
        }
        String infilename = (String) ((ParsedArgument) args[0]).getValue(0);
        PrintStream out;
        if (args.length > 1) {
            String outfile = (String) ((ParsedArgument) args[1]).getValue(0);
            try {
                out = new PrintStream(new FileOutputStream(outfile));
            } catch (FileNotFoundException e) {
                LogManager.v().logError("Failed to open file '" + outfile
                        + "' for writing");
                return;
            }
        } else {
            out = System.out;
        }
        
        try {
            this.extract(infilename, out);
        } catch (IOException e) {
            LogManager.v().logError("Failed to read file: '" + infilename
                    + "'");
        } catch (TraceIOException e) {
            LogManager.v().logError("Failed to read file: '" + infilename
                    + "'");
        }
    }

    public static void main(String[] args) {
        Extractor extractor = new Extractor(args);
        extractor.run();
    }

    public void registerArguments() {
        super.registerArguments();

        // Input file name argument
        Argument in_file = new FileNameArgument(
                ARG_INFILE,   // ID
                "input file", // Display name
                false,        // Required
                false,        // Repeatable
                "Input file", // Short desc
                "Trace or compiled specification input file", //Long desc
                true          // Must exist
        );
        this.registerArgument(in_file);

        // Output file name argument
        Argument out_file = new FileNameArgument(
                ARG_OUTFILE,  // ID
                "output file", // Display name
                false,        // Required
                false,        // Repeatable
                "Output file", // Short desc
                "Specification output file", //Long desc
                false         // Must exist
        );
        this.registerArgument(out_file);
    }

    public void extract(String infile, PrintStream out) throws IOException,
            TraceIOException {
        SpecReader r = new SpecReader(infile);
        (new PrettyPrinter(out)).visit(r.getTraceSpecification());
    }
}
