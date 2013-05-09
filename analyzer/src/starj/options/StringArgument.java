package starj.options;

public class StringArgument extends NamedArgument implements Inlinable {
    private boolean inline;
    
    public StringArgument(int id, String name, String short_desc,
            String long_desc) {
        this(id, name, short_desc, long_desc, false);
    }

    public StringArgument(int id, String name, boolean required,
            String short_desc, String long_desc) {
        this(id, name, required, short_desc, long_desc, false);
    }

    public StringArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc) {
        this(id, name, required, repeatable, short_desc, long_desc, false);
    }

    public StringArgument(int id, String name, String short_desc,
            String long_desc, boolean inline) {
        super(id, name, short_desc, long_desc);
        this.inline = inline;
    }

    public StringArgument(int id, String name, boolean required,
            String short_desc, String long_desc, boolean inline) {
        super(id, name, required, short_desc, long_desc);
        this.inline = inline;
    }

    public StringArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc,
            boolean inline) {
        super(id, name, required, repeatable, short_desc, long_desc);
        this.inline = inline;
    }

    public boolean isInlined() {
        return this.inline;
    }

    protected void setInlined(boolean inline) {
        this.inline = inline;
    }

    public boolean accept(String arg) {
        return true;
    }

    public void parse(String arg, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException {
        if (!this.inline || !(result instanceof ParsedArgument)) {
            result.addChild(new ParsedArgument(this, arg));
        } else {
            ((ParsedArgument) result).addValue(arg);
        }
    }
}
