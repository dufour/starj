package starj.options;

public interface HelpFormat {
    public void formatHelp(Parser parser);
    public void formatUsage(Parser parser);
    public void formatVersion(Parser parser);
}
