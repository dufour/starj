package starj.options;

public abstract class NamedArgument extends FormattableArgument
        implements NamedFormattable {
    private String name;
            
    public NamedArgument(int id, String name, String short_desc,
            String long_desc) {
        super(id, short_desc, long_desc);
    }

    public NamedArgument(int id,String name, boolean required,
            String short_desc, String long_desc) {
        super(id, required, short_desc, long_desc);
        this.name = name;
    }

    public NamedArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc) {
        super(id, required, repeatable, short_desc, long_desc);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
