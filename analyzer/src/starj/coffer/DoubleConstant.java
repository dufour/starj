package starj.coffer;

public class DoubleConstant extends Constant {
    private double value;
    
    public DoubleConstant(double value) {
        super(Constant.CONSTANT_Double);
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }
}
