package starj;

import starj.coffer.types.*;

/**
 * The <code>ApplicationMaster</code> class holds a list of prefixes which
 * determine which classes belong to the library classes and which belong to
 * the application classes.
 *
 * @author Bruno Dufour
 */
public class ApplicationMaster {
    private static ApplicationMaster instance = new ApplicationMaster();

    private String[] library_prefixes = {
        "[", // Array classes
        "java.",
        "javax.",
        "sun.",
        "com.sun.",
        "com.ibm.",
        "org.xml.",
        "org.w3c.",
        "org.apache."
    };

    private ApplicationMaster() {
        // no instances
    }

    public void setLibraryPrefixes(String[] prefixes) {
        this.library_prefixes = (String[]) prefixes.clone();
    }

    public String[] getLibraryPrefixes() {
        return (String[]) this.library_prefixes.clone();
    }

    public boolean contains(String prefix) {
        String[] prefixes = this.library_prefixes;
        for (int i = 0; i < prefixes.length; i++) {
            if (prefix.equals(prefixes[i])) {
                return true;
            }
        }

        return false;
    }

    public void addPrefix(String prefix) {
        if (!this.contains(prefix)) {
            String[] prefixes = this.library_prefixes;
            String[] new_prefixes = new String[prefixes.length + 1];
            System.arraycopy(prefixes, 0, new_prefixes, 0, prefixes.length);
            new_prefixes[prefixes.length] = prefix;
            this.library_prefixes = new_prefixes;
        }
    }

    public boolean isStandardLibrary(String class_name) {
        String[] prefixes = this.library_prefixes;
        for (int i = 0; i < prefixes.length; i++) {
            if (class_name.startsWith(prefixes[i])) {
                return true;
            }
        }

        return false;
    }
    
    public boolean isStandardLibrary(Type type) {
        if (type != null && type.getTypeID() == Type.OBJECT_TYPE) {
            return this.isStandardLibrary(((ObjectType) type).getClassName());
        }
        
        return true;
    }

    public static ApplicationMaster v() {
        return ApplicationMaster.instance;
    }
}
