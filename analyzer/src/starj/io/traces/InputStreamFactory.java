package starj.io.traces;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class InputStreamFactory {
    public static final int DEFAULT_BUFFER_SIZE = 2048;
    
    private static InputStreamFactory instance = null;
    private int buffer_size;
    private File trace_directory;

    private InputStreamFactory() {
        // no instances
        this.buffer_size = DEFAULT_BUFFER_SIZE;
        this.trace_directory = null;
    }
    
    public void setBufferSize(int size) {
        this.buffer_size = size;
    }

    public int getBufferSize() {
        return this.buffer_size;
    }

    public InputStream getInputStream(String filename) throws IOException {
        return this.getInputStream(new File(filename));
    }

    public InputStream getInputStream(File file) throws IOException {
        if (!file.exists()) {
            file = new File(this.trace_directory, file.getName());
        } else {
            this.trace_directory = file.getParentFile();
        }
        
        return this.getInputStream(new FileInputStream(file));
    }
    
    public InputStream getInputStream(InputStream inStream) throws IOException {
        InputStream stream
                = new BufferedInputStream(inStream,
                this.buffer_size);
        stream.mark(4);
        
        int i = stream.read();
        if (i < 0) {
            throw new EOFException();
        }
        int j = stream.read();
        if (i < 0) {
            throw new EOFException();
        }
        
        int magic = ((j) << 8) | i;
        stream.reset();
        
        if (magic == GZIPInputStream.GZIP_MAGIC) {
            stream = new BufferedInputStream(new GZIPInputStream(stream,
                        this.buffer_size), this.buffer_size);
        }
        
        return stream;
    }
    
    public static InputStreamFactory v() {
        if (instance == null) {
            instance = new InputStreamFactory();
        }

        return instance;
    }
}
