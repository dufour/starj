package starj.options;

import java.io.*;

public class FileNameArgument extends NamedArgument {
    private boolean must_exist;

    public FileNameArgument(int id, String name, String short_desc,
            String long_desc) {
        super(id, name, short_desc, long_desc);
        this.must_exist = false;
    }

    public FileNameArgument(int id, String name, boolean required,
            String short_desc, String long_desc) {
        super(id, name, required, short_desc, long_desc);
        this.must_exist = false;
    }

    public FileNameArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc) {
        super(id, name, required, repeatable, short_desc, long_desc);
        this.must_exist = false;
    }

    public FileNameArgument(int id, String name, String short_desc,
            String long_desc, boolean must_exist) {
        super(id, name, short_desc, long_desc);
        this.must_exist = must_exist;
    }

    public FileNameArgument(int id, String name, boolean required,
            String short_desc, String long_desc, boolean must_exist) {
        super(id, name, required, short_desc, long_desc);
        this.must_exist = must_exist;
    }

    public FileNameArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc,
            boolean must_exist) {
        super(id, name, required, repeatable, short_desc, long_desc);
        this.must_exist = must_exist;
    }

    public boolean accept(String arg) {
        return true;
    }

    public void parse(String arg, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException {
        if (this.must_exist) {
            File f = new File(arg);
            if (!(f.exists() && f.isFile() && f.canRead())) {
                throw new OptionProcessingException("File '" + arg
                        + "' does not exist or is not readable.");
            }
        }
        result.addChild(new ParsedArgument(this, arg));
    }
}
