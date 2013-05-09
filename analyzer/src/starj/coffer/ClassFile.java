package starj.coffer;

import java.io.*;

public class ClassFile {
    public static final int MAGIC = 0xCAFEBABE;
    
    public static final short ACC_PUBLIC    = 0x0001;
    public static final short ACC_FINAL     = 0x0010;
    public static final short ACC_SUPER     = 0x0020;
    public static final short ACC_INTERFACE = 0x0200;
    public static final short ACC_ABSTRACT  = 0x0400;

    private int minor_version;
    private int major_version;
    private ConstantPool constant_pool;
    private short access_flags;
    private int this_class;
    private int super_class;
    private int[] interfaces;
    private Field[] fields = null;
    private Method[] methods = null;
    private Attributes attributes = null;

    // Cached values
    private String name;
    private ClassFile[] iface_instances;

    public ClassFile(byte[] b) {
        this(b, LazyConstantFactory.getInstance(),
                AttributeFactory.getInstance());
    }

    public ClassFile(byte[] b, ConstantFactory const_factory) {
        this(b, const_factory, AttributeFactory.getInstance());
    }

    public ClassFile(byte[] b, AttributeFactory attr_factory) {
        this(b, LazyConstantFactory.getInstance(), attr_factory);
    }

    public ClassFile(byte[] b, ConstantFactory const_factory,
            AttributeFactory attr_factory) {
        this(new DataInputStream(new BufferedInputStream(
                new ByteArrayInputStream(b))), const_factory, attr_factory);
    }

    public ClassFile(DataInput input) {
        this(input, LazyConstantFactory.getInstance(),
                AttributeFactory.getInstance());
    }

    public ClassFile(DataInput input, ConstantFactory factory) {
        this(input, factory, AttributeFactory.getInstance());
    }

    public ClassFile(DataInput input, AttributeFactory factory) {
        this(input, LazyConstantFactory.getInstance(), factory);
    }

    public ClassFile(DataInput input, ConstantFactory const_factory,
            AttributeFactory attr_factory) {
        try {
            int magic = input.readInt();
            if (magic != MAGIC) {
                throw new ClassFileFormatException("Invalid magic number");
            }
            this.minor_version = input.readUnsignedShort();
            this.major_version = input.readUnsignedShort();
            this.constant_pool = new ConstantPool(input, const_factory);
            this.access_flags = input.readShort();
            this.this_class = input.readUnsignedShort();
            this.super_class = input.readUnsignedShort();
            this.parseInterfaces(input);
            this.parseFields(input, attr_factory);
            this.parseMethods(input, attr_factory);
            this.attributes = new Attributes(input, this.constant_pool,
                    attr_factory);
        } catch (IOException e) {
            throw new ClassFileFormatException("Invalid class file");
        }
    }

    private void parseInterfaces(DataInput input) throws IOException {
        int iface_count = input.readUnsignedShort();
        int[] ifaces = new int[iface_count];
        for (int i = 0; i < iface_count; i++) {
            ifaces[i] = input.readUnsignedShort();
        }
        this.interfaces = ifaces;
    }

    private void parseFields(DataInput input, AttributeFactory factory)
                throws IOException {
        int field_count = input.readUnsignedShort();
        Field[] fields = new Field[field_count];
        for (int i = 0; i < field_count; i++) {
            fields[i] = new Field(
                this,
                input.readShort(),
                input.readUnsignedShort(),
                input.readUnsignedShort(),
                new Attributes(input, this.constant_pool, factory)
            );
        }
        this.fields = fields;
    }

    private void parseMethods(DataInput input, AttributeFactory factory)
                throws IOException {
        int method_count = input.readUnsignedShort();
        Method[] methods = new Method[method_count];
        for (int i = 0; i < method_count; i++) {
            methods[i] = new Method(
                this,
                input.readShort(),
                input.readUnsignedShort(),
                input.readUnsignedShort(),
                new Attributes(input, this.constant_pool, factory)
            );
        }
        this.methods = methods;
    }

    // Accessor methods
    
    public int getMagic() {
        return ClassFile.MAGIC;
    }

    public int getMinorVersion() {
        return this.minor_version;
    }

    public int getMajorVersion() {
        return this.major_version;
    }

    public ConstantPool getConstantPool() {
        return this.constant_pool;
    }

    public short getAccessFlags() {
        return this.access_flags;
    }

    public boolean isInterface() {
        return ((this.access_flags & ACC_INTERFACE) != 0);
    }

    public int getThisClassIndex() {
        return this.this_class;
    }

    public String getName() {
        if (this.name == null) {
            this.name = this.constant_pool.getClassName(this.this_class);
        }
        return this.name;
    }

    public int getSuperClassIndex() {
        return this.super_class;
    }

    public String getSuperClassName() {
        if (this.super_class == 0
                && "java.lang.Object".equals(this.getName())) {
            // java.lang.Object
            return null;
        }

        return this.constant_pool.getClassName(this.super_class);
    }

    public ClassFile getSuperClass() {
        String super_class_name = this.getSuperClassName();
        if (super_class_name != null) {
            return Repository.v().lookup(this.getSuperClassName());
        }

        return null;
    }

    public int getInterfaceCount() {
        return (this.interfaces != null ? this.interfaces.length : 0);
    }

    public ClassFile[] getInterfaces() {
        if (this.iface_instances == null) {
            int iface_count = this.getInterfaceCount();
            this.iface_instances = new ClassFile[iface_count];
            for (int i = 0; i < iface_count; i++) {
                this.iface_instances[i] = this.getInterface(i);
            }
        }

        return this.iface_instances;
    }

    public String getInterfaceName(int index) {
        int iface_index = this.interfaces[index];
        return this.constant_pool.getClassName(iface_index);
    }

    public ClassFile getInterface(int index) {
        return Repository.v().lookup(this.getInterfaceName(index));
    }

    public int getFieldCount() {
        return (this.fields != null ? this.fields.length : 0);
    }

    public int getMethodCount() {
        return (this.methods != null ? this.methods.length : 0);
    }

    public Field[] getFields() {
        return this.fields;
    }

    public Field getField(int index) {
        return this.fields[index];
    }

    public Method[] getMethods() {
        return this.methods;
    }

    public Method getMethod(int index) {
        return this.methods[index];
    }

    public String toString() {
        return this.getName();
    }

    public String prettyPrint() {
        String rv = this.getModifierString();
        if (rv.length() > 0) {
            rv += " ";
        }

        boolean is_interface = this.isInterface();
        if (is_interface) {
            rv += "interface ";
        } else {
            rv += "class ";
        }
        rv += this.getName();
        if (!is_interface) {
            String super_class_name = this.getSuperClassName();
            if (super_class_name != null) {
                rv += " extends " + super_class_name;
            }
        }
        int iface_count = this.getInterfaceCount();
        if (iface_count > 0) {
            if (is_interface) {
                rv += " extends ";
            } else {
                rv += " implements ";
            }

            for (int i = 0; i < iface_count; i++) {
                rv += this.getInterfaceName(i);
                if (i < iface_count - 1) {
                    rv += ", ";
                }
            }
        }
        rv += " {\n";
        if (this.methods != null) {
            for (int i = 0; i < this.methods.length; i++) {
                rv += "    " + this.methods[i].prettyPrint() + "\n";
            }
        }
        rv += "}\n";
        return rv;
    }

    public boolean definesMethod(String method_name, String method_signature) {
        return this.getMethod(method_name, method_signature) != null;
    }

    public boolean definesMethod(String full_name) {
        return this.getMethod(full_name) != null;
    }

    public Method getMethod(String full_name) {
        if (this.methods != null) {
            for (int i = 0; i < this.methods.length; i++) {
                Method m = this.methods[i];
                if (full_name.equals(m.getFullName())) {
                    return m;
                }
            }
        }

        return null;
    }

    public Method getMethod(String method_name, String method_signature) {
        return this.getMethod(method_name + method_signature);
    }

    public String getModifierString() {
        short access_flags = this.getAccessFlags();
        String rv;
        if ((access_flags & ACC_PUBLIC) != 0) {
            rv = "public";
        } else {
            rv = "";
        }

        if ((access_flags & ACC_FINAL) != 0) {
            rv += " final";
        }

        if (rv.length() > 0 && rv.charAt(0) == ' ') {
            rv = rv.substring(1);
        }

        return rv;
    }

    public boolean hasMethod(String method_name, String method_signature) {
        return this.lookupMethod(method_name, method_signature) != null;
    }

    public boolean hasMethod(String full_name) {
        return this.lookupMethod(full_name) != null;
    }

    public Method lookupMethod(String method_name, String method_signature) {
        return this.lookupMethod(method_name + method_signature);
    }

    public Method lookupMethod(String full_name) {
        ClassFile c;
        if (this.isInterface()) {
            Method m = this.getMethod(full_name);
            if (m != null) {
                return m;
            }

            ClassFile[] interfaces = this.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                m = interfaces[i].lookupMethod(full_name);
                if (m != null) {
                    return m;
                }
            }

            // Super-interface lookup failed, try super class
            // (i.e. fall through)
            c = this.getSuperClass();
        } else {
            c = this;
        }

        while (c != null) {
            Method m = c.getMethod(full_name);
            if (m != null) {
                return m;
            }

            c = c.getSuperClass();
        }

        return null;
    }

    public boolean isSubclassOf(ClassFile c) {
        String super_class_name = c.getName();
        ClassFile tmp = this.getSuperClass();
        while (tmp != null) {
            if (super_class_name.equals(tmp.getName())) {
                return true;
            }
            tmp = tmp.getSuperClass();
        }

        return false;
    }

    public boolean isSuperClassOf(ClassFile c) {
        String super_class_name = this.getName();
        ClassFile tmp = c.getSuperClass();
        while (tmp != null) {
            if (super_class_name.equals(tmp.getName())) {
                return true;
            }
            tmp = tmp.getSuperClass();
        }

        return false;
    }

    public boolean equals(Object obj) {
        return this.getName().equals(((ClassFile) obj).getName());
    }

    public boolean isImplementationOf(ClassFile iface) {
        if (!iface.isInterface()) {
            return false;
        }

        ClassFile[] ifaces = this.getInterfaces();
        if (ifaces != null) {
            for (int i = 0; i < ifaces.length; i++) {
                ClassFile candidate = ifaces[i];
                if (iface.getName().equals(candidate.getName())) {
                    // Easy case -- exact match
                    return true;
                }

                // Does this interface extend the desired interface?
                if (candidate.isImplementationOf(iface)) {
                    // yes, so we are done
                    return true;
                }
            }
        }

        ClassFile super_class = this.getSuperClass();
        if (super_class != null) {
            return super_class.isImplementationOf(iface);
        }

        return false;
    }
}
