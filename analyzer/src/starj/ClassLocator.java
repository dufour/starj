package starj;

import starj.coffer.Repository;
import starj.io.logging.LogManager;
import starj.options.*;

public class ClassLocator extends ClassPathApplication {
    public static final String DESC_STRING = "*J class locator";
    public static final int ARG_CLASS_NAME = 0;

    public ClassLocator(String[] args) {
        super(DESC_STRING, args);
    }

    public void main(ParsedResult[] args) {
        for (int i = 0; i < args.length; i++) {
            ParsedArgument arg = (ParsedArgument) args[i];
            String class_name = (String) arg.getValue(0);
            String location = Repository.v().locate(class_name);
            LogManager.v().logMessage("Class '" + class_name + "'"
                    + (location != null ? (" found in '" + location + "'.")
                                        : " not found."));
        }
    }

    public static void main(String[] args) {
        ClassLocator main = new ClassLocator(args);
        main.run();
    }

    public void registerArguments() {
        super.registerArguments();

        Argument class_arg = new StringArgument(
                ARG_CLASS_NAME, // ID
                "class_name",   // Name
                false,          // Required
                true,           // Repeatable
                "Class name to locate",                // Short description
                "Fully qualified class name to locate" // Long description
        );
        this.registerArgument(class_arg);
    }
}
