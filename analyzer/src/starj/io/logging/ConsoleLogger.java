package starj.io.logging;

public class ConsoleLogger implements Logger {
    private int mask;

    public ConsoleLogger() {
        this(~0);
    }

    public ConsoleLogger(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return this.mask;
    }
    
    public void logPlain(String message) {
        System.out.println(message);
    }
    
    public void logMessage(String message){
        System.out.println("*J> " + message);
    }

    public void logWarning(String message){
        System.err.println("*J Warning> " + message);
    }

    public void logError(String message){
        System.err.println("*J Error> " + message);
    }
    
    public void logDebug(String message){
        System.out.println("*J Debug> " + message);
    }
}
