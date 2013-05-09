package starj.io;

import java.io.*;

public class StderrWrapper implements PrintStreamWrapper {
    public StderrWrapper() {
        super();
    }

    public PrintStream getStream() {
        return System.err;
    }

    public void closeStream() {

    }
}
