package starj;

import starj.coffer.*;
import starj.io.logging.*;
import starj.options.*;

public class ShowClass extends ClassPathApplication {
    public static final String DESC_STRING = "*J class printer";
    public static final int ARG_CLASS_NAME = 0;

    public ShowClass(String[] args) {
        super(DESC_STRING, args);
    }

    public void main(ParsedResult[] args) {
        for (int i = 0; i < args.length; i++) {
            ParsedArgument arg = (ParsedArgument) args[i];
            String class_name = (String) arg.getValue(0);
            ClassFile class_file = Repository.v().lookup(class_name);
            if (class_file != null) {
                System.out.println(class_file.prettyPrint());
            } else {
                LogManager.v().logError("Class '" + class_name
                        + "' not found");
            }
        }
    }

    public static void main(String[] args) {
        ShowClass main = new ShowClass(args);
        main.run();
    }

    public void registerArguments() {
        super.registerArguments();

        Argument class_arg = new StringArgument(
                ARG_CLASS_NAME, // ID
                "class_name",   // Name
                false,          // Required
                false,           // Repeatable
                "Class name to locate",                // Short description
                "Fully qualified class name to locate" // Long description
        );
        this.registerArgument(class_arg);
    }
}
