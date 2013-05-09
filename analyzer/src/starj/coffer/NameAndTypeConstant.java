package starj.coffer;

public class NameAndTypeConstant extends Constant {
    private int name_index;
    private int descriptor_index;

    public NameAndTypeConstant(int name_index, int descriptor_index) {
        super(Constant.CONSTANT_NameAndType);
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
    }

    public int getNameIndex() {
        return this.name_index;
    }

    public int getDescriptorIndex() {
        return this.descriptor_index;
    }
}
