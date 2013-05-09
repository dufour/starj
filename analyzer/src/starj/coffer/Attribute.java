package starj.coffer;

public abstract class Attribute {
    private String name;
    private ConstantPool cp;
    
    public Attribute(String name, ConstantPool cp) {
        this.name = name;
        this.cp = cp;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isConcrete() {
        return true; // Overriden in LazyAttribute
    }
    
    public ConstantPool getConstantPool() {
        return this.cp;
    }
}
