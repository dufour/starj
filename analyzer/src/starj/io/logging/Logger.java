package starj.io.logging;

public interface Logger {
    // Error logging
    public static final int ERROR       = 0x00000001;
    // Warning logging
    public static final int WARNING     = 0x00000002;
    // Debug logging
    public static final int DEBUG       = 0x00000004;
    // Message logging
    public static final int CRITICAL    = 0x00000008;
    public static final int IMPORTANT   = 0x00000010;
    public static final int VALUABLE    = 0x00000020;
    public static final int INFORMATIVE = 0x00000040;
    public static final int NEUTRAL     = 0x00000080;
    public static final int VERBOSE     = 0x00000100;
    public static final int WORDY       = 0x00000200;
    public static final int SUPERFLUOUS = 0x00000400;
    public static final int EXCESSIVE   = 0x00000800;
    
    public int getMask();
    public void logPlain(String message);
    public void logMessage(String message);
    public void logWarning(String message);
    public void logError(String message);
    public void logDebug(String message);
}
