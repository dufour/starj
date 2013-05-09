package starj.io.logging;

import starj.io.ConsoleColor;

public class ColorConsoleLogger implements Logger {
    private ConsoleColor message_color;
    private ConsoleColor warning_color;
    private ConsoleColor error_color;
    private ConsoleColor debug_color;
    private int mask;

    public ColorConsoleLogger() {
        this(~0);
    }
    
    public ColorConsoleLogger(int mask) {
        this.mask = mask;
        setMessageColor(null);
        setWarningColor(ConsoleColor.TURQUOISE);
        setErrorColor(ConsoleColor.RED);
        setDebugColor(ConsoleColor.YELLOW);
    }

    public int getMask() {
        return this.mask;
    }

    public void setMessageColor(ConsoleColor color) {
        if (color != null) {
            this.message_color = color;
        } else {
            this.message_color = ConsoleColor.NONE;
        }
    }

    public void setWarningColor(ConsoleColor color) {
        if (color != null) {
            this.warning_color = color;
        } else {
            this.warning_color = ConsoleColor.NONE;
        }
    }

    public void setErrorColor(ConsoleColor color) {
        if (color != null) {
            this.error_color = color;
        } else {
            this.error_color = ConsoleColor.NONE;
        }
    }

    public void setDebugColor(ConsoleColor color) {
        if (color != null) {
            this.debug_color = color;
        } else {
            this.debug_color = ConsoleColor.NONE;
        }
    }

    public void logPlain(String message) {
        System.out.println(message);
    }

    public void logMessage(String message){
        String s = this.message_color
            + "*J> "
            + message
            + ConsoleColor.RESET;
        System.out.println(s);
    }

    public void logWarning(String message){
        String s = this.warning_color
            + "*J Warning> "
            + message
            + ConsoleColor.RESET;
        System.err.println(s);
    }

    public void logError(String message){
        String s = this.error_color
            + "*J Error> "
            + message
            + ConsoleColor.RESET;
        System.err.println(s);
    }
    
    public void logDebug(String message){
        String s = this.debug_color
            + "*J Debug> "
            + message
            + ConsoleColor.RESET;
        System.err.println(s);
    }
}
