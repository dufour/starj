package starj.io.traces;

import java.io.*;

public class TraceReaderFactory {
    private static TraceReaderFactory instance;
    
    private TraceReaderFactory() {
        // no instances
    }

    public TraceReader getTraceReader(InputStream stream)
            throws IOException, TraceIOException {
        return new PoolTraceReader(stream);

        // The following code works fine when not using
        // GZIP'ed traces. GZIPInputStream instances don't
        // play nice with mark() and reset(), and so
        // the following will actually *not* reset a
        // GZIPInputStream properly.
        //
        //trace_stream.mark(64);
        //int magic = trace_stream.readInt();
        //if (magic == PoolTraceReader.TRACE_MAGIC) {
        //    trace_stream.reset();
        //    return new PoolTraceReader(trace_stream);
        //}
        //throw new TraceFormatException("Unrecognized trace format");
    }

    public static TraceReaderFactory v() {
        if (instance == null) {
            instance = new TraceReaderFactory();
        }
        return instance;
    }
}
