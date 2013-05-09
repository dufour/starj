package starj.coffer;

public class FloatConstant extends Constant {
    private float value;
    
    public FloatConstant(float value) {
        super(Constant.CONSTANT_Float);
        this.value = value;
    }

    public float getValue() {
        return this.value;
    }
}
