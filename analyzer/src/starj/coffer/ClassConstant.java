package starj.coffer;

public class ClassConstant extends Constant {
    private int name_index;
    
    public ClassConstant(int name_index) {
        super(Constant.CONSTANT_Class);
        this.name_index = name_index;
    }

    public int getNameIndex() {
        return this.name_index;
    }
}
