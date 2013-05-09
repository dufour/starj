package starj.toolkits.services;

import starj.coffer.*;

public class StackFrame {
    private MethodEntity method_entity;

    public StackFrame(MethodEntity method_entity) {
        this.method_entity = method_entity;
    }
    
    public ConstantPool getConstantPool() {
        Method m = this.getMethod();
        return (m != null ? m.getClassFile().getConstantPool() : null);
    }
    
    public MethodEntity getMethodEntity() {
        return this.method_entity;
    }
    
    public Method getMethod() {
        MethodEntity me = this.method_entity;
        return (me != null ? me.getMethod() : null);
    }
    
    public int hashCode() {
        if (this.method_entity != null) {
            return this.method_entity.hashCode();
        }
        
        return 0;
    }
    
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (!(obj instanceof StackFrame)) {
            return false;
        }
        
        return this.matches(((StackFrame) obj).method_entity);
    }
    
    public boolean matches(MethodEntity method_entity) {
        MethodEntity me = this.method_entity;
        return me == method_entity || (me != null && me.equals(method_entity));
    }
}
