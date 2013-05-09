package starj.toolkits.services;

import starj.ApplicationMaster;
import starj.coffer.ClassFile;
import starj.coffer.types.Type;
import starj.StorageFactory;

public class ClassEntity extends TypedEntity {
    private static StorageFactory storage_factory = new StorageFactory();
    private ObjectEntity object_entity;
    private ClassFile class_file;
    private String class_name;
    private String source_name;
    private int num_interfaces;
    private MethodEntity[] methods;
    // FIXME: instance fields
    // FIXME: static fields

    public ClassEntity(int id, ObjectEntity object_entity, 
            ClassFile class_file, String class_name,
            String source_name, int num_interfaces, MethodEntity[] methods,
            Type type, boolean authoritative) {
        super(id, ApplicationMaster.v().isStandardLibrary(class_name), type,
                authoritative);
        this.object_entity = object_entity;
        this.class_file = class_file;
        this.class_name = class_name;
        this.source_name = source_name;
        this.num_interfaces = num_interfaces;
        this.methods = methods;
    }

    public ObjectEntity getObjectEntity() {
        return this.object_entity;
    }

    public ClassFile getClassFile() {
        return this.class_file;
    }
    
    public void setClassFile(ClassFile class_file) {
        this.class_file = class_file;
    }
    
    public String getClassName() {
        return this.class_name;
    }
    
    public String getSourceName() {
        return this.source_name;
    }
    
    public int getInterfaceCount() {
        return this.num_interfaces;
    }

    public int getMethodCount() {
        return (this.methods != null ? this.methods.length : 0);
    }
    
    public MethodEntity[] getMethods() {
        return this.methods;
    }
    
    public MethodEntity getMethod(int index) {
        return this.methods[index];
    }

    public void moveTo(int new_class_id) {
        this.setID(new_class_id);
    }
    
    public static int registerFlag() {
        return ClassEntity.storage_factory.registerFlag();
    }
    
    public static int registerCounter() {
        return ClassEntity.storage_factory.registerCounter();
    }
    
    public static int registerStorageSpace() {
        return ClassEntity.storage_factory.registerStorageSpace();
    }
    
    public StorageFactory getStorageFactory() {
        return ClassEntity.storage_factory;
    }
}
