package starj.io;

import java.io.*;

public interface PrintStreamWrapper {
    public abstract PrintStream getStream() throws IOException;
    public abstract void closeStream();
}
