package starj.debug;

import java.io.*;

import starj.*;
import starj.events.Event;
import starj.io.logging.*;
import starj.io.traces.*;
import starj.options.*;
import starj.toolkits.services.IDResolver;

public class TraceValidator extends ClassPathApplication {
    public static final int OPTION_BUFFER_SIZE    =  0;
    public static final int ARG_FILENAME          =  0;

    public TraceValidator(String[] args) {
        super(Main.DESC_STRING, args);
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
            LogManager.v().logMessage("Starting validity test");
            this.readFully(reader);
            LogManager.v().logMessage("Trace validity test PASSED",
                    Logger.IMPORTANT);
        } catch (Exception e) {
            LogManager.v().logError(e.toString());
            e.printStackTrace();
            LogManager.v().logError("Trace validity check FAILED");
        }
    }
    
    private void readFully(TraceReader reader) throws IOException,
            TraceIOException {
        IDResolver r = IDResolver.v();
        EventBox box = new SingleEventBox();
        r.init();
        Event e = null;
        long event_count = 0L;
        try {
            while (true) {
                e = reader.getNextEvent();
                if (e == null) {
                    break;
                }
                
                event_count++;
    
                box.setEvent(e);
                try {
                    IDResolver.v().apply(box);
                } catch (EventSkipException except) {
                    
                }
            }
        } finally {
            LogManager.v().logMessage("Read " + event_count
                    + (event_count > 1L ? " events" : "event"));
        }
        r.done();
    }

    public static void main(String[] args) {
        TraceValidator validator = new TraceValidator(args);
        validator.run();
    }

    public void registerArguments() {
        super.registerArguments();

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
            case OPTION_BUFFER_SIZE: {
                    Integer value = (Integer) parsed_option.getValue(0, 0);
                    int buff_size = value.intValue();
                    InputStreamFactory.v().setBufferSize(buff_size);
                    LogManager.v().logMessage("User buffer size = "
                            + buff_size);
                }
                break;
            default:
                super.handleOption(parsed_option);
        }
    }
}
