package starj.io;

import java.io.*;

public class StdoutWrapper implements PrintStreamWrapper {
    public StdoutWrapper() {
        super();
    }

    public PrintStream getStream() {
        return System.out;
    }

    public void closeStream() {

    }
}
