package starj.io.logging;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class LogManager {
    private static LogManager instance;
    private List loggers;
    //private int level;

    private LogManager() {
        // no instances
        this.loggers = new LinkedList();
        this.loggers.add(new ConsoleLogger());
        //this.level = Logger.NEUTRAL;
    }

    public boolean isEmpty() {
        return this.loggers.size() == 0;
    }

    public void clear() {
        this.loggers = new LinkedList();
    }

    public void addLogger(Logger logger) {
        if (!this.loggers.contains(logger)) {
            this.loggers.add(logger);
        }
    }

    public void removeLogger(Logger logger) {
        this.loggers.remove(logger);
    }

    // public void setLevel(int level) {
    //     this.level = level;
    // }

    // public int getLevel() {
    //     return this.level;
    // }
    
    private boolean check_mask(Logger logger, int level) {
        int mask = logger.getMask();
        return ((mask & level) == level);
    }

    public void logPlain(String message) {
        this.logPlain(message, Logger.NEUTRAL);
    }

    public void logPlain(String message, int level) {
        for (Iterator i = this.loggers.iterator(); i.hasNext(); ) {
            Logger logger = (Logger) i.next();
            if (this.check_mask(logger, level)) {
                logger.logPlain(message);
            }
        }
    }

    public void logMessage(String message) {
        this.logMessage(message, Logger.NEUTRAL);
    }

    public void logMessage(String message, int level) {
        for (Iterator i = this.loggers.iterator(); i.hasNext(); ) {
            Logger logger = (Logger) i.next();
            if (this.check_mask(logger, level)) {
                logger.logMessage(message);
            }
        }
    }

    public void logWarning(String message) {
        for (Iterator i = this.loggers.iterator(); i.hasNext(); ) {
            Logger logger = (Logger) i.next();
            if (this.check_mask(logger, Logger.WARNING)) {
                logger.logWarning(message);
            }
        }
    }

    public void logError(String message) {
        for (Iterator i = this.loggers.iterator(); i.hasNext(); ) {
            Logger logger = (Logger) i.next();
            if (this.check_mask(logger, Logger.ERROR)) {
                logger.logError(message);
            }
        }
    }

    public void logError(Throwable t) {
        this.logError(t.getMessage());
    }

    public void logDebug(String message) {
        for (Iterator i = this.loggers.iterator(); i.hasNext(); ) {
            Logger logger = (Logger) i.next();
            if (this.check_mask(logger, Logger.DEBUG)) {
                logger.logDebug(message);
            }
        }
    }

    public static LogManager v() {
        if (instance == null) {
            instance = new LogManager();
        }
        
        return instance;
    }
}
