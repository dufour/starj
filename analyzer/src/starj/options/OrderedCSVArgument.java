package starj.options;

public class OrderedCSVArgument extends CSVArgument {
    public OrderedCSVArgument(int id, String name, String short_desc,
            String long_desc, boolean inline) {
        super(id, name, short_desc, long_desc, true, inline);
    }

    public OrderedCSVArgument(int id, String name, boolean required,
            String short_desc, String long_desc, boolean inline) {
        super(id, name, required, short_desc, long_desc, true, inline);
    }

    public OrderedCSVArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc,
            boolean inline) {
        super(id, name, required, repeatable, short_desc, long_desc, true,
                inline);
    }
}
