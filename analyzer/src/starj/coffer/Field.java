package starj.coffer;

public class Field extends Member {
    public static final short ACC_PUBLIC    = 0x0001;
    public static final short ACC_PRIVATE   = 0x0002;
    public static final short ACC_PROTECTED = 0x0004;
    public static final short ACC_STATIC    = 0x0008;
    public static final short ACC_FINAL     = 0x0010;
    public static final short ACC_VOLATILE  = 0x0040;
    public static final short ACC_TRANSIENT = 0x0080;

    public Field(ClassFile class_file, short access_flags, int name_index,
            int descriptor_index, Attributes attributes) {
        super(class_file, access_flags, name_index, descriptor_index,
                attributes);
    }
}
