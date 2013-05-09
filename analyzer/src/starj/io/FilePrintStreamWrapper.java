package starj.io;

import java.io.*;

public class FilePrintStreamWrapper implements PrintStreamWrapper {
    private String filename;
    private PrintStream stream;

    public FilePrintStreamWrapper(String filename) {
        this.filename = filename;
    }
    
    public FilePrintStreamWrapper(File file) {
        this(file.getAbsolutePath());
    }

    public PrintStream getStream() throws IOException {
        this.stream = new PrintStream(new FileOutputStream(this.filename));
        return this.stream;
    }

    public void closeStream() {
        if (this.stream != null) {
            this.stream.close();
        }
    }

    protected synchronized void finalize() {
        this.closeStream();
    }
}
