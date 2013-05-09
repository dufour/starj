package starj.coffer;

public class LineNumberTableAttribute extends Attribute {
    public static final String ATTRIBUTE_NAME = "LineNumberTable";

    private LineNumber[] line_number_table;
    
    public LineNumberTableAttribute(ConstantPool cp,
            LineNumber[] line_number_table) {
        super(LineNumberTableAttribute.ATTRIBUTE_NAME, cp);
        this.line_number_table = line_number_table;
    }

    public LineNumber get(int index) {
        return this.line_number_table[index];
    }

    public LineNumber[] get() {
        return this.line_number_table;
    }
}
