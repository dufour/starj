package starj.toolkits.printers;

import java.io.*;

import starj.AbstractOperation;
import starj.io.PrintStreamWrapper;
import starj.options.*;

public abstract class AbstractPrinter extends AbstractOperation {
    protected PrintStream out;
    protected PrintStreamWrapper wrapper;

    public AbstractPrinter(String name, String description) {
        this(name, description, null);
    }

    public AbstractPrinter(String name, String description, PrintStream out) {
        super(name, description);
        this.out = out;
    }
    
    public void init() {
        if (this.out == null) {
            if (this.wrapper != null) {
                try {
                    this.out = this.wrapper.getStream();
                } catch (IOException e) {
                    throw new RuntimeException(e); // FIXME
                }
            } else {
                this.out = System.out;
            }
        }
    }

    public void configure(ElementConfigArgument config, Object value) {
        String name = config.getName();
        if (name.equals("file")) {
            if (value != null) {
                this.wrapper = (PrintStreamWrapper) value;
            }
        } else {
            super.configure(config, value);
        }
    }

    public ElementConfigSet getConfigurationSet() {
        ElementConfigSet set = super.getConfigurationSet();
        ElementConfigArgument file_arg = new ElementConfigArgument(
                0,
                "file",
                "Sets the output file name",
                "Sets the name of a file to use as output",
                false
        );
        file_arg.addArgument(new StreamNameArgument(
                0,
                "stream",
                true,  // required
                false, // not repeatable
                "Stream name",
                "Stream name (stdout, stderr, file:<filename> or filename)"
        ));
        set.addConfig(file_arg);
        return set;
    }
}
