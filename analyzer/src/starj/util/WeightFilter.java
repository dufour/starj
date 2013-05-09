package starj.util;

public class WeightFilter implements Filter {
    private WeightFunction f;
    
    public WeightFilter(WeightFunction f) {
        this.f = f;
    }
    
    public boolean keep(Object object) {
        return this.f.getValue(object) > 0L;
    }
}