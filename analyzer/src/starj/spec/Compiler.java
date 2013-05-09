package starj.spec;

import java.io.*;

import starj.*;
import starj.io.logging.LogManager;
import starj.options.*;


public class Compiler extends Application implements TraceSpecificationVisitor {
    public static final String DESC_STRING = "*J Event specification compiler";
    private static final int ARG_INFILE  = 0;
    private static final int ARG_OUTFILE = 1;
    
    private DataOutput out;

    public Compiler(String[] args) {
        super(DESC_STRING, args);
    }

    public void main(ParsedResult[] args) {
        if (args.length < 1) {
            this.printUsage();
            System.exit(0);
        }
        String infilename = (String) ((ParsedArgument) args[0]).getValue(0);
        String outfilename;
        if (args.length > 1) {
            outfilename = (String) ((ParsedArgument) args[1]).getValue(0);
        } else {
            int i = infilename.lastIndexOf('.');
            if (i >= 0) {
                outfilename = infilename.substring(0, i);
            } else {
                outfilename = infilename;
            }
            outfilename += ".spec";
        }
        
        this.compile(infilename, outfilename);
    }

    public static void main(String[] args) {
        Compiler compiler = new Compiler(args);
        compiler.run();
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
                "Specification input file", //Long desc
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
                "Compiled specification output file", //Long desc
                false         // Must exist
        );
        this.registerArgument(out_file);
    }
    
    public void compile(String infile, String outfile) {
        Reader r;
        try {
            FileInputStream inStream = new FileInputStream(infile);
            r = new BufferedReader(new InputStreamReader(inStream));
        } catch (FileNotFoundException e) {
            LogManager.v().logError("File not found: '" + infile + "'");
            return;
        }
        
        TraceSpecification spec = null;
        try {
            LogManager.v().logMessage("Parsing file: '" + infile + "'");
            Lexer lexer = new Lexer(r);
            Parser parser = new Parser(lexer);
            AST ast = parser.parse();
            SpecFormat jvmpi_format = SpecFormatManager.v().getCurrentFormat();
            ASTExpander expander = new ASTExpander(jvmpi_format);
            spec = expander.expand(ast);
            LogManager.v().logMessage("Parsing successful");
        } catch (InvalidSyntaxException e) {
            LogManager.v().logError(e);
            System.exit(1);
        } catch (IOException e) {
            LogManager.v().logError("Error while reading file: '" + infile
                    + "'");
            System.exit(2);
        }

        DataOutputStream out;
        try {
            FileOutputStream outStream = new FileOutputStream(outfile);
            out = new DataOutputStream(outStream);
            try {
                LogManager.v().logMessage("Writing output to: '" + outfile
                        + "'");
                this.compile(spec, out);
                LogManager.v().logMessage("Compilation successful");
            } finally {
                out.close();
            }
        } catch (IOException e) {
            LogManager.v().logError("Failed to write to file '" + outfile
                    + "'");
            System.exit(3);
        }

        
    }

    public void compile(TraceSpecification trace, DataOutput output) {
        this.out = output;
        this.visit(trace);
    }
    
    public void visit(TraceSpecification trace) {
        EventSpecification[] events = trace.getEvents();
        try {
            this.out.writeInt(Constants.MAGIC);
            this.out.writeShort(Constants.MINOR_VERSION);
            this.out.writeShort(Constants.MAJOR_VERSION);
            this.out.writeInt(events.length);
            for (int i = 0; i < events.length; i++) {
                this.visitEvent(events[i]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Compilation failed", e);
        }
    }
    
    private int getDependencyMask(FieldDefinition def) {
        int mask = 0;
        
        FieldDefinition[] deps = def.getDependencies();
        if (deps != null && deps.length > 0) {
            for (int i = 0; i < deps.length; i++) {
                FieldDefinition dep = deps[i];
                mask |= dep.getMask() | this.getDependencyMask(dep);
            }
        }
        
        return mask;
    }

    public void visitEvent(EventSpecification event) throws IOException {
        EventDefinition def = event.getDefinition();
        this.out.writeByte((byte) (def.getID() & 0x000000FF));

        int mask = 0;
        FieldSpecification[] fields = event.getFields();
        for (int i = 0; i < fields.length; i++) {
            FieldSpecification field = fields[i];
            FieldDefinition field_def = field.getDefinition();
            mask |= field_def.getMask() | this.getDependencyMask(field_def);
        } 

        this.out.writeInt(mask);
    }
}
