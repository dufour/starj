package starj.options;

import starj.io.*;

public class StreamNameArgument extends NamedArgument {
    public StreamNameArgument(int id, String name, String short_desc,
            String long_desc) {
        super(id, name, short_desc, long_desc);
    }

    public StreamNameArgument(int id, String name, boolean required,
            String short_desc, String long_desc) {
        super(id, name, required, short_desc, long_desc);
    }

    public StreamNameArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc) {
        super(id, name, required, repeatable, short_desc, long_desc);
    }

    public boolean accept(String arg) {
        return true;
    }

    public void parse(String arg, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException {
        if (arg.equals("stdout") || arg.equals("out")) {
            result.addChild(new ParsedArgument(this, new StdoutWrapper()));
        } else if (arg.equals("stderr") || arg.equals("err")) {
            result.addChild(new ParsedArgument(this, new StderrWrapper()));
        } else if (arg.startsWith("file:")) {
            result.addChild(new ParsedArgument(this,
                    new FilePrintStreamWrapper(arg.substring(5))));
        } else {
            result.addChild(new ParsedArgument(this,
                    new FilePrintStreamWrapper(arg)));
        }
    }
}
