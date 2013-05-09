package starj.coffer;

public class IntegerConstant extends Constant {
    private int value;
    
    public IntegerConstant(int value) {
        super(Constant.CONSTANT_Integer);
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
