package starj.options;

import starj.Scene;
import starj.HierarchyElement;

public class PatternArgument extends NamedArgument implements Inlinable {
    private boolean inline;

    public PatternArgument(int id, String name, String short_desc,
            String long_desc) {
        this(id, name, short_desc, long_desc, false);
    }

    public PatternArgument(int id, String name, boolean required,
            String short_desc, String long_desc) {
        this(id, name, required, short_desc, long_desc, false);
    }

    public PatternArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc) {
        this(id, name, required, repeatable, short_desc, long_desc, false);
    }

    public PatternArgument(int id, String name, String short_desc,
            String long_desc, boolean inline) {
        super(id, name, short_desc, long_desc);
        this.inline = inline;
    }

    public PatternArgument(int id, String name, boolean required,
            String short_desc, String long_desc, boolean inline) {
        super(id, name, required, short_desc, long_desc);
        this.inline = inline;
    }

    public PatternArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc,
            boolean inline) {
        super(id, name, required, repeatable, short_desc, long_desc);
        this.inline = inline;
    }

    public boolean isInlined() {
        return this.inline;
    }

    public boolean accept(String argument) {
        return true;
    }

    public void parse(String arg, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException {
        ParsedArgument parsed_arg;
        if (!this.inline || !(result instanceof ParsedArgument)) {
            parsed_arg = new ParsedArgument(this);
            result.addChild(parsed_arg);
        } else {
            parsed_arg = (ParsedArgument) result;
        }
        HierarchyElement[] elements = Scene.v().getRootPack().findElements(arg);
        if (elements == null || elements.length <= 0) {
            throw new OptionProcessingException("No match for '" + arg + "'");
        } else {
            for (int i = 0; i < elements.length; i++) {
                parsed_arg.addValue(elements[i]);
            }
        }
    }
}
