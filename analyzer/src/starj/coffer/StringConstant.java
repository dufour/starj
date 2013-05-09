package starj.coffer;

public class StringConstant extends Constant {
    private int string_index;
    
    public StringConstant(int string_index) {
        super(Constant.CONSTANT_String);
        this.string_index = string_index;
    }

    public int getStringIndex() {
        return this.string_index;
    }
}
