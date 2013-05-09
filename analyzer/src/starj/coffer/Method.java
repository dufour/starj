package starj.coffer;

import starj.coffer.types.*;

public class Method extends Member {
    public static final short ACC_PUBLIC       = 0x0001;
    public static final short ACC_PRIVATE      = 0x0002;
    public static final short ACC_PROTECTED    = 0x0004;
    public static final short ACC_STATIC       = 0x0008;
    public static final short ACC_FINAL        = 0x0010;
    public static final short ACC_SYNCHRONIZED = 0x0020;
    public static final short ACC_NATIVE       = 0x0100;
    public static final short ACC_ABSTRACT     = 0x0400;
    public static final short ACC_STRICT       = 0x0800;

    private String full_name;
    private Code code;
    private int jvmpi_id;
    
    public Method(ClassFile class_file, short access_flags, int name_index,
            int descriptor_index, Attributes attributes) {
        super(class_file, access_flags, name_index, descriptor_index,
                attributes);
        CodeAttribute attrib = (CodeAttribute)
                attributes.lookupFirst(CodeAttribute.ATTRIBUTE_NAME);
        if (attrib != null) {
            this.code = attrib.getCode();
        }
    }

    public String getFullName() {
        if (this.full_name == null) {
            this.full_name = this.getName() + this.getDescriptor();
        }

        return this.full_name;
    }

    public Code getCode() {
        return this.code;
    }

    public Type getReturnType() {
        String signature = this.getDescriptor();
        String ret_type = signature.substring(signature.indexOf(')') + 1);
        return TypeRepository.v().getType(ret_type);
    }

    public Type[] getParameterTypes() {
        String signature = this.getDescriptor();
        String arg_type_str = signature.substring(1, signature.indexOf(')'));
        return TypeRepository.v().parseTypes(arg_type_str);
    }

    public String toString() {
        return this.getClassFile() + "." + this.getFullName();
    }

    public String prettyPrint() {
        String rv = this.getModifierString();
        if (rv.length() > 0) {
            rv += " ";
        }
        rv += this.getReturnType() + " ";
        rv += this.getName();
        rv += "(";
        Type[] param_types = this.getParameterTypes();
        for (int i = 0; i < param_types.length; i++) {
            if (i > 0) {
                rv += ", ";
            }
            rv += param_types[i];
        }
        rv += ");";
        return rv;
    }
    
    public boolean isPublic() {
        return ((this.getAccessFlags() & ACC_PUBLIC) != 0);
    }
    
    public boolean isPrivate() {
        return ((this.getAccessFlags() & ACC_PRIVATE) != 0);
    }
    
    public boolean isProtected() {
        return ((this.getAccessFlags() & ACC_PROTECTED) != 0);
    }
    
    public boolean isAbstract() {
        return ((this.getAccessFlags() & ACC_ABSTRACT) != 0);
    }
    
    public boolean isStatic() {
        return ((this.getAccessFlags() & ACC_STATIC) != 0);
    }
    
    public boolean isFinal() {
        return ((this.getAccessFlags() & ACC_FINAL) != 0);
    }
    
    public boolean isSynchronized() {
        return ((this.getAccessFlags() & ACC_SYNCHRONIZED) != 0);
    }
    
    public boolean isNative() {
        return ((this.getAccessFlags() & ACC_NATIVE) != 0);
    }
    
    public boolean isStrict() {
        return ((this.getAccessFlags() & ACC_STRICT) != 0);
    }    
    
    public String getModifierString() {
        short access_flags = this.getAccessFlags();
        String rv;
        if ((access_flags & ACC_PUBLIC) != 0) {
            rv = "public";
        } else if ((access_flags & ACC_PRIVATE) != 0) {
            rv = "private";
        } else if ((access_flags & ACC_PROTECTED) != 0) {
            rv = "protected";
        } else {
            rv = "";
        }

        if ((access_flags & ACC_ABSTRACT) != 0) {
            rv += " abstract";
        }
        if ((access_flags & ACC_STATIC) != 0) {
            rv += " static";
        }
        if ((access_flags & ACC_FINAL) != 0) {
            rv += " final";
        }
        if ((access_flags & ACC_SYNCHRONIZED) != 0) {
            rv += " synchronized";
        }
        if ((access_flags & ACC_NATIVE) != 0) {
            rv += " native";
        }
        if ((access_flags & ACC_STRICT) != 0) {
            rv += " strictfp";
        }

        if (rv.length() > 0 && rv.charAt(0) == ' ') {
            rv = rv.substring(1);
        }

        return rv;
    }
}
