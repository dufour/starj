package starj.options;

import java.util.regex.*;

public abstract class RealNumberArgument extends NumberArgument {
    private static final Matcher matcher
            = Pattern.compile("^(([+-]?[1-9][0-9]*)|0)(\\.[0-9]+$").matcher("");
    
    public RealNumberArgument(int id, String name, String short_desc,
            String long_desc) {
        super(id, name, short_desc, long_desc);
    }

    public RealNumberArgument(int id, String name, boolean required,
            String short_desc, String long_desc) {
        super(id, name, required, short_desc, long_desc);
    }

    public RealNumberArgument(int id, String name, boolean required,
            boolean repeatable, String short_desc, String long_desc) {
        super(id, name, required, repeatable, short_desc, long_desc);
    }

    public boolean accept(String arg) {
        matcher.reset(arg);
        return matcher.matches();
    }
}
