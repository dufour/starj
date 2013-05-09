package starj.io;

public class ConsoleColor {
    public static final ConsoleColor NONE       = new ConsoleColor(null);
    public static final ConsoleColor RESET      = new ConsoleColor("0");
    public static final ConsoleColor BOLD       = new ConsoleColor("01");
    public static final ConsoleColor TURQUOISE  = new ConsoleColor("36;01");
    public static final ConsoleColor TEAL       = new ConsoleColor("36;06");
    public static final ConsoleColor FUSCIA     = new ConsoleColor("35;01");
    public static final ConsoleColor PURPLE     = new ConsoleColor("35;06");
    public static final ConsoleColor BLUE       = new ConsoleColor("34;01");
    public static final ConsoleColor DARK_BLUE  = new ConsoleColor("34;06");
    public static final ConsoleColor YELLOW     = new ConsoleColor("33;01");
    public static final ConsoleColor BROWN      = new ConsoleColor("33;06");
    public static final ConsoleColor GREEN      = new ConsoleColor("32;01");
    public static final ConsoleColor DARK_GREEN = new ConsoleColor("32;06");
    public static final ConsoleColor RED        = new ConsoleColor("31;01");
    public static final ConsoleColor DARK_RED   = new ConsoleColor("31;06");

    private String color_string;
    
    private ConsoleColor(String color_code) {
        if (color_code != null) {
            this.color_string = "\u001b[" + color_code + "m";
        } else {
            this.color_string = "";
        }
    }

    public String toString() {
        return this.color_string;
    }
}
