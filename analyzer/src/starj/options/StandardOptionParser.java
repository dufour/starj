package starj.options;

public class StandardOptionParser extends Parser {
    private DelimiterManager delim_manager;
    private OptionManager option_manager;
    
    public StandardOptionParser() {
        super();

        // Delimiter support 
        this.delim_manager = new DelimiterManager();
        this.addManager(this.delim_manager);

        // Option support
        this.option_manager = new OptionManager();
        this.addManager(this.option_manager);

        // Collect non-option arguments together at the end of the list
        this.addPostProcessor(new ArgumentCollector());
    }

    public void addArgument(Argument argument) {
        if (argument instanceof Option) {
            this.option_manager.addOption((Option) argument);
        } else {
            super.addArgument(argument);
        }
    }
}
