package starj.options;

public class ParsedOption extends ParsedArgument {
    public ParsedOption(Option option) {
        super(option);
    }

    public ParsedOption(Option option, Object value) {
        super(option, value);
    }

    public Option getOption() {
        return (Option) this.getArgument();
    }
}
