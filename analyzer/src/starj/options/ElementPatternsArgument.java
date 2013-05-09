package starj.options;

public class ElementPatternsArgument extends UnorderedCSVArgument {
    public ElementPatternsArgument(int id, String name, String short_desc,
            String long_desc, boolean inline) {
        super(id, name, short_desc, long_desc, inline);
        this.init();
    }

    public ElementPatternsArgument(int id, String name, boolean required,
            String short_desc, String long_desc, boolean inline) {
        super(id, name, required, short_desc, long_desc, inline);
        this.init();
    }

    public ElementPatternsArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc,
            boolean inline) {
        super(id, name, required, repeatable, short_desc, long_desc, inline);
        this.init();
    }

    private void init() {
        Argument element_names_arg = new PatternArgument(
                0,
                "name",
                true, // Required
                true, // Repeatable
                "Name pattern",
                "Name pattern specifying which elements to be configured",
                true  // Inlined
        );
        super.addArgument(element_names_arg);
    }

    public void addArgument(Argument argument) {
        throw new RuntimeException(this.getClass().getName()
                + " class does not allow adding new arguments");
    }
}
