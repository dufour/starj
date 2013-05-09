package starj.options;

import java.io.File;

public class PathArgument extends NamedArgument {
    public PathArgument(int id, String name, String short_desc,
            String long_desc) {
        super(id, name, short_desc, long_desc);
    }

    public PathArgument(int id, String name, boolean required,
            String short_desc, String long_desc) {
        super(id, name, required, short_desc, long_desc);
    }

    public PathArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc) {
        super(id, name, required, repeatable, short_desc, long_desc);
    }

    public boolean accept(String arg) {
        return true;
    }

    public void parse(String arg, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException {
        ParsedArgument parsed_arg = new ParsedArgument(this);
        result.addChild(parsed_arg);
        String[] paths = arg.split(File.pathSeparator);
        for (int i = 0; i < paths.length; i++) {
            parsed_arg.addValue(paths[i]);
        }
    }
}
