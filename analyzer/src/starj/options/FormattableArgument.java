package starj.options;

public abstract class FormattableArgument extends Argument
        implements Formattable {
    private String short_desc;
    private String long_desc;
    
    public FormattableArgument(int id, String short_desc, String long_desc) {
        super(id);
        this.short_desc = short_desc;
        this.long_desc = long_desc;
    }

    public FormattableArgument(int id, boolean required, String short_desc,
            String long_desc) {
        super(id, required);
        this.short_desc = short_desc;
        this.long_desc = long_desc;
    }

    public FormattableArgument(int id, boolean required, boolean repeatable,
            String short_desc, String long_desc) {
        super(id, required, repeatable);
        this.short_desc = short_desc;
        this.long_desc = long_desc;
    }

    public String getShortDescription() {
        return this.short_desc;
    }

    public String getLongDescription() {
        return this.long_desc;
    }
}
