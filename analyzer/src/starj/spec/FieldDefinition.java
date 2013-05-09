package starj.spec;

import java.util.*;

public class FieldDefinition {
    private String name;
    private int mask;
    private Set dependencies;
    
    public FieldDefinition(String name, int mask) {
        this.name = name;
        this.mask = mask;
        this.dependencies = new HashSet();
    }
    
    public FieldDefinition(String name, int mask,
            FieldDefinition dependency) {
        this(name, mask);
        this.addDependency(dependency);
    }

    public String getName() {
        return this.name;
    }

    public int getMask() {
        return this.mask;
    }
    
    public void addDependency(FieldDefinition field) {
        this.dependencies.add(field);
    }

    public FieldDefinition[] getDependencies() {
        FieldDefinition[] rv
                = new FieldDefinition[this.dependencies.size()];
        rv = (FieldDefinition[]) this.dependencies.toArray(rv);
        return rv;
    }
}
