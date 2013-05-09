package starj;

import starj.coffer.Repository;
import starj.options.*;

public abstract class ClassPathApplication extends Application {
    public static final int OPTION_CP = -100;

    public ClassPathApplication(String description, String[] args) {
        super(description, args);
    }

    public void registerArguments() {
        super.registerArguments();

        // Class path option
        Option classpath_opt = new Option(
            OPTION_CP,
            "General",
            "Sets the class path",
            "Sets the path used to locate class files"
        );
        classpath_opt.addShortSwitch("cp");
        classpath_opt.addShortSwitch("classpath");
        classpath_opt.addLongSwitch("classpath");
        classpath_opt.addArgument(new PathArgument(
                0,            // ID
                "classpath",  // name
                true,         // required?
                false,        // repeatable?
                "Class path", // short desc
                "Class path"  // long desc
        ));
        this.registerArgument(classpath_opt);
    }

    public void handleOption(ParsedOption parsed_option) {
        switch (parsed_option.getID()) {
            case OPTION_CP: {
                    Object[] paths = parsed_option.getValues(0); 
                    String[] cp = new String[paths.length];
                    for (int i = 0; i < paths.length; i++) {
                        cp[i] = (String) paths[i];
                    }
                    Repository.v().setClassPath(cp);
                }
                break;
            default:
                super.handleOption(parsed_option);
                break;
        }
    }
}
