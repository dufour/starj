package starj.coffer;

public class SyntheticAttribute extends Attribute {
    public static final String ATTRIBUTE_NAME = "Synthetic";

    public SyntheticAttribute(ConstantPool cp) {
        super(SyntheticAttribute.ATTRIBUTE_NAME, cp);
    }
}
