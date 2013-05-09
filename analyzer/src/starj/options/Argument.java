package starj.options;

public abstract class Argument {
    private int id;
    private boolean repeatable;
    private boolean required;

    public Argument(int id) {
        this(id, true);
    }

    public Argument(int id, boolean required) {
        this(id, required, false);
    }

    public Argument(int id, boolean required, boolean repeatable) {
        this.id = id;
        this.required = required;
        this.repeatable = repeatable;
    }
    
    public int getID() {
        return this.id;
    }

    public boolean isRepeatable() {
        return this.repeatable;
    }

    public boolean isRequired() {
        return this.required;
    }

    public abstract boolean accept(String arg);

    public abstract void parse(String arg, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException;

    public String toString() {
        return this.getClass().getName() + " #" + this.id
            + " (" + (this.required ? "!" : "")
            + (this.repeatable ? "*" : "") + ")";
    }
}
