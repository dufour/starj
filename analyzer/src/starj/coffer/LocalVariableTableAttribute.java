package starj.coffer;

public class LocalVariableTableAttribute extends Attribute {
    public static final String ATTRIBUTE_NAME = "LocalVariableTable";

    private LocalVariable[] local_variable_table;
    
    public LocalVariableTableAttribute(ConstantPool cp,
            LocalVariable[] local_variable_table) {
        super(LocalVariableTableAttribute.ATTRIBUTE_NAME, cp);
        this.local_variable_table = local_variable_table;
    }

    public LocalVariable get(int index) {
        return this.local_variable_table[index];
    }

    public LocalVariable[] get() {
        return this.local_variable_table;
    }
}
