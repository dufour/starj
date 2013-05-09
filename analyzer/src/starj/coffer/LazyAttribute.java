package starj.coffer;

public class LazyAttribute extends GenericAttribute {
    public LazyAttribute(ConstantPool cp, int name_index, byte[] data) {
        super(cp, name_index, data);
    }
    
    public Attribute concretize() {
        return null;
    }

    public boolean isConcrete() {
        return false;
    }
}
