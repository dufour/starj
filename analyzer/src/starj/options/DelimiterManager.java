package starj.options;

public class DelimiterManager extends AbstractArgumentManager {
    public static final String DELIM = "--";
    
    public DelimiterManager() {
        this(false);
    }
    
    public DelimiterManager(boolean return_delimiters) {
        super();
        super.addArgument(new Delimiter(return_delimiters));
    }

    public void addArgument(Argument argument) {
        throw new RuntimeException("Cannot add more separators");
    }
    
    public boolean accept(String argument) {
        return DELIM.equals(argument);
    }

    private class Delimiter extends Argument {
        private boolean return_delimiters;

        Delimiter(boolean return_delimiters) {
            super(0, false, false);
            this.return_delimiters = return_delimiters;
        }

        public boolean accept(String argument) {
            return DelimiterManager.DELIM.equals(argument);
        }

        public void parse(String arg, ParsedResult result, StringQueue queue,
                Parser parser) throws OptionProcessingException {
            if (this.return_delimiters) {
                result.addChild(new ParsedArgument(this, arg));
            }
            parser.disableExtensionManagers();
        }
    }
}
