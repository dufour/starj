package starj.coffer;

public class InnerClassesAttribute extends Attribute {
    public static final String ATTRIBUTE_NAME = "InnerClasses";

    private InnerClass[] classes;
    
    public InnerClassesAttribute(ConstantPool cp, InnerClass[] classes) {
        super(InnerClassesAttribute.ATTRIBUTE_NAME, cp);
        this.classes = classes;
    }

    public InnerClass get(int index) {
        return this.classes[index];
    }

    public InnerClass[] get() {
        return this.classes;
    }
}
