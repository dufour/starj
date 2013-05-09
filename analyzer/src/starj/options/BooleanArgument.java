package starj.options;

public class BooleanArgument extends NamedArgument {
    private static final String[] TRUE_VALUES = {"true", "on", "yes"};
    private static final String[] FALSE_VALUES = {"false", "off", "no"};

    public BooleanArgument(int id, String name, String short_desc,
            String long_desc) {
        super(id, name, short_desc, long_desc);
    }

    public BooleanArgument(int id, String name, boolean required,
            String short_desc, String long_desc) {
        super(id, name, required, short_desc, long_desc);
    }

    public BooleanArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc) {
        super(id, name, required, repeatable, short_desc, long_desc);
    }

    public boolean accept(String arg) {
        for (int i = 0; i < TRUE_VALUES.length; i++) {
            if (arg.equalsIgnoreCase(TRUE_VALUES[i])) {
                return true;
            }
        }

        for (int i = 0; i < FALSE_VALUES.length; i++) {
            if (arg.equalsIgnoreCase(FALSE_VALUES[i])) {
                return true;
            }
        }
        return false;
    }

    public void parse(String arg, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException {
        for (int i = 0; i < TRUE_VALUES.length; i++) {
            if (arg.equalsIgnoreCase(TRUE_VALUES[i])) {
                result.addChild(new ParsedArgument(this, Boolean.TRUE));
                return;
            }
        }

        for (int i = 0; i < FALSE_VALUES.length; i++) {
            if (arg.equalsIgnoreCase(FALSE_VALUES[i])) {
                result.addChild(new ParsedArgument(this, Boolean.FALSE));
                return;
            }
        }

        // Should not happen if accept() is called first
        throw new OptionProcessingException("Invalid boolean value: '"
                + arg + "'");
    }
}
