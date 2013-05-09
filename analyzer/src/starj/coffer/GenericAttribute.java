package starj.coffer;

public class GenericAttribute extends Attribute {
    private int name_index;
    private byte[] data;

    public GenericAttribute(ConstantPool cp, int name_index, byte[] data) {
        super(cp.getUtf8(name_index), cp);
        this.name_index = name_index;
        this.data = data;
    }

    public int getNameIndex() {
        return this.name_index;
    }

    public byte[] getData() {
        return this.data;
    }
}
