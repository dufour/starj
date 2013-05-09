package starj.coffer;

import java.util.*;
import java.io.*;
import java.util.zip.*;

import starj.io.logging.LogManager;

public class Repository {
    private Map cache;
    private static Repository instance;
    private ClassPathEntry[] classpath;
    private ClassFileFactory class_factory;

    private Repository() {
        this.classpath = null;
        this.class_factory = new ClassFileFactory();
        this.reset();
    }

    private Repository(String[] classpath) {
        this();
        this.setClassPath(classpath);
    }

    public void setClassPath(String[] classpath) {
        // Set class path
        this.buildClasspath(classpath);
        // Clear cache
        this.reset();
    }

    public ClassFileFactory getClassFileFactory() {
        return this.class_factory;
    }

    public void setClassFileFactory(ClassFileFactory factory) {
        this.class_factory = factory;
    }

    public void reset() {
        this.cache = new HashMap();
    }

    private void buildClasspath(String[] classpath) {
        if (classpath == null) {
            // Get system class path
            String java_classpath = System.getProperty("java.class.path");
            String boot_classpath = System.getProperty("sun.boot.class.path");
            if (boot_classpath != null) {
                java_classpath += File.pathSeparator + boot_classpath;
            }
            if (java_classpath != null) {
                classpath = java_classpath.split(File.pathSeparator);
            } else {
                classpath = new String[0];
            }
        }
        
        List entries = new LinkedList();
        Set entry_locations = new HashSet();
        
        for (int i = 0; i < classpath.length; i++) {
            String location = classpath[i];
            File f = new File(location);

            // Make sure the entry exists
            if (!(f.exists() && f.canRead())) {
                LogManager.v().logWarning("Class path entry '"
                        + location + "' does not exist or is not readable");
                continue;
            }

            // Obtain the canonical path for this entry
            File canonical_file = null;
            try {
                canonical_file = f.getCanonicalFile();
            } catch (IOException e) {
                LogManager.v().logWarning("Failed to obtain the canonical "
                        + "name for '" + location + "'");
                continue;
            }

            if (!entry_locations.add(canonical_file.getPath())) {
                LogManager.v().logWarning("Removing duplicate class path "
                        + " entry: '" + location + "'");
                continue;
            }

            if (f.isDirectory()) {
                entries.add(new DirectoryEntry(canonical_file));
            } else if (location.endsWith(".jar")
                    || location.endsWith(".zip")) {
                try {
                    entries.add(new ArchiveEntry(canonical_file.getPath()));
                } catch (ZipException e) {
                    LogManager.v().logWarning("Class path entry '"
                            + location
                            + "' does not point to a valid archive");
                } catch (IOException e) {

                }
            }
        }

        this.classpath = new ClassPathEntry[entries.size()];
        entries.toArray(this.classpath);
    }

    public boolean contains(String class_name) {
        if (this.cache.containsKey(class_name)) {
            return true;
        }

        if (this.classpath == null) {
            this.setClassPath(null);
        }

        for (int i = 0; i < this.classpath.length; i++) {
            ClassPathEntry entry = this.classpath[i];
            if (entry.contains(class_name)) {
                return true;
            }
        }

        return false;
    }

    public InputStream getInputStream(String class_name) {
        if (this.classpath == null) {
            this.setClassPath(null);
        }

        InputStream rv = null;
        for (int i = 0; i < this.classpath.length; i++) {
            ClassPathEntry entry = this.classpath[i];
            
            rv = entry.getInputStream(class_name);
            if (rv != null) {
                break;
            }
        }

        return rv;
    }

    public ClassFile lookup(String class_name) {
        if (this.cache.containsKey(class_name)) {
            return (ClassFile) this.cache.get(class_name);
        }

        if (this.classpath == null) {
            this.setClassPath(null);
        }
        
        for (int i = 0; i < this.classpath.length; i++) {
            ClassPathEntry entry = this.classpath[i];
            InputStream is = entry.getInputStream(class_name);
            if (is != null) {
                ClassFile class_file = this.class_factory.newClassFile(
                        new DataInputStream(is));
                this.cache.put(class_name, class_file);
                return class_file;
            }
        }

        return null;
    }

    public String locate(String class_name) {
        if (this.classpath == null) {
            this.setClassPath(null);
        }
        
        String rv = null;
        for (int i = 0; i < this.classpath.length; i++) {
            ClassPathEntry entry = this.classpath[i];
            
            rv = entry.locate(class_name);
            if (rv != null) {
                break;
            }
        }

        return rv;
    }

    public static Repository v() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    private abstract class ClassPathEntry {
        private String location;
        
        ClassPathEntry(String location) {
            this.location = location;
        }

        public String getLocation() {
            return this.location;
        }

        public abstract boolean contains(String class_name);
        public abstract InputStream getInputStream(String class_name);
        public abstract String locate(String class_name);
    }

    private class DirectoryEntry extends ClassPathEntry {
        private File file;
        
        DirectoryEntry(String location) {
            this(new File(location));
        }

        DirectoryEntry(File file) {
            super(file.getPath());
            this.file = file;
        }

        public boolean contains(String class_name) {
            char sep = File.separatorChar;

            File candidate = new File(this.file, class_name.replace('.', sep)
                    + ".class");
            return (candidate.exists() && candidate.isFile());
        }

        public InputStream getInputStream(String class_name) {
            char sep = File.separatorChar;

            File candidate = new File(this.file, class_name.replace('.', sep)
                    + ".class");
            if (candidate.exists() && candidate.isFile()) {
                try {
                    return new FileInputStream(candidate);
                } catch (FileNotFoundException e) {
                    // Should not happen, since we check for existence first
                    // Ignoring is safe even without the check, since
                    // we just fall back to the default behaviour
                }
            }

            return null;
        }

        public String locate(String class_name) {
            char sep = File.separatorChar;

            File candidate = new File(this.file, class_name.replace('.', sep)
                    + ".class");
            if (candidate.exists() && candidate.isFile()) {
                return candidate.getPath();
            }

            return null;
        }
    }

    private class ArchiveEntry extends ClassPathEntry {
        private ZipFile zip;
        private Map name_map;

        ArchiveEntry(String location)  throws ZipException, IOException {
            super(location);
            this.zip = new ZipFile(location);
            this.name_map = null;
        }

        public boolean contains(String class_name) {
            if (this.name_map == null) {
                this.name_map = new HashMap();

                Enumeration zip_entries = this.zip.entries();
                while (zip_entries.hasMoreElements()) {
                    ZipEntry zip_entry = (ZipEntry) zip_entries.nextElement();
                    String name = zip_entry.getName();
                    if (name != null && name.endsWith(".class")) {
                        // Get class name
                        String cname = name.substring(0,
                                name.length() - 6).replace('/', '.');
                        this.name_map.put(cname, zip_entry);
                    }
                }
            }

            return this.name_map.containsKey(class_name);
        }

        public InputStream getInputStream(String class_name) {
            if (this.contains(class_name)) {
                ZipEntry zip_entry = (ZipEntry) this.name_map.get(class_name);
                try {
                    return this.zip.getInputStream(zip_entry);
                } catch (IOException e) {
                    // Fall back to default behaviour
                }
            }

            return null;
        }

        public String locate(String class_name) {
            if (this.contains(class_name)) {
                return this.getLocation();
            }

            return null;
        }
    }
}
