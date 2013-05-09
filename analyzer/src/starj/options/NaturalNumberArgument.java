package starj.options;

import java.util.regex.*;

public abstract class NaturalNumberArgument extends NumberArgument {
    private static final Matcher matcher
            = Pattern.compile("^(([+-]?[1-9][0-9]*)|0)$").matcher("");
    
    public NaturalNumberArgument(int id, String name, String short_desc,
            String long_desc) {
        super(id, name, short_desc, long_desc);
    }

    public NaturalNumberArgument(int id, String name, boolean required,
            String short_desc, String long_desc) {
        super(id, name, required, short_desc, long_desc);
    }

    public NaturalNumberArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc) {
        super(id, name, required, repeatable, short_desc, long_desc);
    }

    public boolean accept(String arg) {
        matcher.reset(arg);
        return matcher.matches();
    }
}
