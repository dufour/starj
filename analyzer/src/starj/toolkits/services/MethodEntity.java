package starj.toolkits.services;

import starj.coffer.Method;
import starj.events.*;
import starj.util.IntToLongHashMap;
import starj.StorageFactory;

public class MethodEntity extends Entity {
    private static StorageFactory storage_factory = new StorageFactory();
    
    private ClassEntity class_entity;
    private String method_name;
    private String method_signature;
    private int start_lineno;
    private int end_lineno;
    private Method method;

    private IntToLongHashMap entry_counts;

    public MethodEntity(int id, ClassEntity class_entity, String method_name,
            String method_signature, int start_lineno, int end_lineno,
            Method method) {
        this(id, class_entity, method_name, method_signature, start_lineno,
                end_lineno, method, true);
    }
    
    public MethodEntity(int id, ClassEntity class_entity, String method_name,
            String method_signature, int start_lineno, int end_lineno,
            Method method, boolean authoritative) {
        super(id, class_entity.isStandardLibrary(), authoritative);
        this.class_entity = class_entity;
        this.method_name = method_name;
        this.method_signature = method_signature;
        this.start_lineno = start_lineno;
        this.end_lineno = end_lineno;
        this.method = method;

        this.entry_counts = new IntToLongHashMap(5, 0L);
    }
    
    public MethodEntity(ClassEntity class_entity, JVMPIMethod jvmpi_method,
            Method method) {
        this(class_entity, jvmpi_method, method, true); 
    }
    
    public MethodEntity(ClassEntity class_entity, JVMPIMethod jvmpi_method,
            Method method, boolean authoritative) {
        this(jvmpi_method.getMethodID(), class_entity, 
                jvmpi_method.getMethodName(), jvmpi_method.getMethodSignature(),
                jvmpi_method.getStartLineno(), jvmpi_method.getEndLineno(),
                method, authoritative);
    }
    
    public ClassEntity getClassEntity() {
        return this.class_entity;
    }

    public String getMethodName() {
        return this.method_name;
    }

    public String getMethodSignature() {
        return this.method_signature;
    }

    public int getStartLineno() {
        return this.start_lineno;
    }

    public int getEndLineno() {
        return this.end_lineno;
    }

    public Method getMethod() {
        return this.method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    void enter(int env_id) {
        this.entry_counts.put(env_id, this.entry_counts.get(env_id) + 1L);
    }

    boolean exit(int env_id) {
        long curr_count = this.entry_counts.get(env_id);
        if (curr_count > 0L) {
            this.entry_counts.put(env_id, curr_count - 1L);
            return true;
        }

        return false;
    }

    public long getEntryCount(int env_id) {
        return this.entry_counts.get(env_id);
    }

    public String toString() {
        if (this.method != null) {
            return this.method.toString();
        }

        return this.method_name + this.method_signature;
    }
    
    public static int registerFlag() {
        return MethodEntity.storage_factory.registerFlag();
    }
    
    public static int registerCounter() {
        return MethodEntity.storage_factory.registerCounter();
    }
    
    public static int registerStorageSpace() {
        return MethodEntity.storage_factory.registerStorageSpace();
    }

    public StorageFactory getStorageFactory() {
        return MethodEntity.storage_factory;
    }
}
