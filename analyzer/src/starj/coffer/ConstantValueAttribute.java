package starj.coffer;

public class ConstantValueAttribute extends Attribute {
    public static final String ATTRIBUTE_NAME = "ConstantValue";

    private int constantvalue_index;
    
    public ConstantValueAttribute(ConstantPool cp, int constantvalue_index) {
        super(ConstantValueAttribute.ATTRIBUTE_NAME, cp);
        this.constantvalue_index = constantvalue_index;
    }

    public int getConstantValueIndex() {
        return this.constantvalue_index;
    }
}
