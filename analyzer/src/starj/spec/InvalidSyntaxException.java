package starj.spec;

public class InvalidSyntaxException extends Exception {
    private int lineNumber;
    private String message;
    
    public InvalidSyntaxException(int lineNumber) {
        this.lineNumber = lineNumber;
        this.message = "";
    }

    public InvalidSyntaxException(int lineNumber, String message) {
        this.lineNumber = lineNumber;
        this.message = message;
    }

    public String toString() {
        return "Syntax error at line " + this.lineNumber + ": " + this.message;
    }
}
