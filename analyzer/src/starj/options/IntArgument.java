package starj.options;

public class IntArgument extends NaturalNumberArgument {
    public IntArgument(int id, String name, String short_desc,
            String long_desc) {
        super(id, name, short_desc, long_desc);
    }

    public IntArgument(int id, String name, boolean required,
            String short_desc, String long_desc) {
        super(id, name, required, short_desc, long_desc);
    }

    public IntArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc) {
        super(id, name, required, repeatable, short_desc, long_desc);
    }

    public void parse(String arg, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException {
        result.addChild(new ParsedArgument(this, new Integer(arg)));
    }
}
