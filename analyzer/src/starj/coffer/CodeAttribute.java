package starj.coffer;

public class CodeAttribute extends Attribute {
    public static final String ATTRIBUTE_NAME = "Code";

    private int max_stack;
    private int max_locals;
    private Code code;
    private ExceptionTableEntry[] exception_table;
    private Attributes attributes;
    
    public CodeAttribute(ConstantPool cp, int max_stack, int max_locals, 
            Code code, ExceptionTableEntry[] exception_table,
            Attributes attributes) {
        super(CodeAttribute.ATTRIBUTE_NAME, cp);
        this.max_stack = max_stack;
        this.max_locals = max_locals;
        this.code = code;
        this.exception_table = exception_table;
        this.attributes = attributes;
    }
    
    public int getMaxStack() {
        return this.max_stack;
    }
    
    public int getMaxLocals() {
        return this.max_locals;
    }
    
    public Code getCode() {
        return this.code;
    }
    
    public ExceptionTableEntry[] getExceptionTable() {
        return this.exception_table;
    }
    
    public ExceptionTableEntry getExceptionTable(int index) {
        return this.exception_table[index];
    }
    
    public Attributes getAttributes() {
        return this.attributes;
    }
    
    public Attribute getAttribute(int index) {
        return this.attributes.getAttribute(index);
    }
}
