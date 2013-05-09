package starj.coffer;

public class DeprecatedAttribute extends Attribute {
    public static final String ATTRIBUTE_NAME = "Deprecated";

    public DeprecatedAttribute(ConstantPool cp) {
        super(DeprecatedAttribute.ATTRIBUTE_NAME, cp);
    }
}
