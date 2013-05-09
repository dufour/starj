package starj.coffer;

public class Utf8Constant extends Constant {
    private String value;
    
    public Utf8Constant(String value) {
        super(Constant.CONSTANT_Utf8);
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
