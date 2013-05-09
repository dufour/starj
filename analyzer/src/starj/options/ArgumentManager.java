package starj.options;

import java.lang.String;

public interface ArgumentManager extends MutableArgumentContainer {
    public void init();
    public void done();
    public boolean missing(Argument argument);
    public boolean enabled(Argument argument);
    public void addArgument(Argument argument);
    public boolean accept(String argument);
    public void parse(String argument, ParsedResult result,
            StringQueue queue, Parser parser) throws OptionProcessingException;
}
