package starj.options;

/**
 * An abstract argument class which from which numerical arguments
 * are derived.
 */
public abstract class NumberArgument extends NamedArgument {
    public NumberArgument(int id, String name, String short_desc,
            String long_desc) {
        super(id, name, short_desc, long_desc);
    }

    public NumberArgument(int id, String name, boolean required,
            String short_desc, String long_desc) {
        super(id, name, required, short_desc, long_desc);
    }

    public NumberArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc) {
        super(id, name, required, repeatable, short_desc, long_desc);
    }
}
