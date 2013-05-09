package starj.coffer;

public class SourceFileAttribute extends Attribute {
    public static final String ATTRIBUTE_NAME = "SourceFile";

    private int sourcefile_index;

    public SourceFileAttribute(ConstantPool cp, int sourcefile_index) {
        super(SourceFileAttribute.ATTRIBUTE_NAME, cp);
        this.sourcefile_index = sourcefile_index;
    }

    public int getSourceFileIndex() {
        return this.sourcefile_index;
    }
}
