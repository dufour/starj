package starj.coffer;

public class ExceptionsAttribute extends Attribute {
    public static final String ATTRIBUTE_NAME = "Exceptions";

    private int[] exceptions;
    
    public ExceptionsAttribute(ConstantPool cp, int[] exceptions) {
        super(ExceptionsAttribute.ATTRIBUTE_NAME, cp);
        this.exceptions = exceptions;
    }

    public int get(int index) {
        return this.exceptions[index];
    }

    public int[] get() {
        return this.exceptions;
    }
}
