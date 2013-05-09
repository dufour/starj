package starj.coffer;

public abstract class Member {
    private ClassFile class_file;
    private short access_flags;
    private int name_index;
    private int descriptor_index;
    private Attributes attributes;

    public Member(ClassFile class_file, short access_flags, int name_index,
            int descriptor_index, Attributes attributes) {
        this.class_file = class_file;
        this.access_flags = access_flags;
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
        this.attributes = attributes;
    }

    public ClassFile getClassFile() {
        return this.class_file;
    }
    
    public short getAccessFlags() {
        return this.access_flags;
    }

    public String getName() {
        ConstantPool cp = this.class_file.getConstantPool();
        return cp.getUtf8(this.name_index);
    }
    
    public int getNameIndex() {
        return this.name_index;
    }
    
    public String getDescriptor() {
        ConstantPool cp = this.class_file.getConstantPool();
        return cp.getUtf8(this.descriptor_index);
    }
    
    public int getDescriptorIndex() {
        return this.descriptor_index;
    }
    
    public Attributes getAttributes() {
        return this.attributes;
    }
    
    public Attribute getAttribute(int index) {
        return this.attributes.getAttribute(index);
    }
}
