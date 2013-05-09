package starj.toolkits.services;

import starj.StorageFactory;

public class ThreadEntity extends Entity {
    private static StorageFactory storage_factory = new StorageFactory();
    private ObjectEntity object_entity;
    private String thread_name;
    private String parent_name;
    private String group_name;
    private ThreadEntity parent;

    public ThreadEntity(int id, ObjectEntity object_entity, String thread_name,
            String parent_name, String group_name, boolean authoritative) {
        this(id, object_entity, thread_name, parent_name, group_name, null,
                authoritative);
    }
    
    public ThreadEntity(int id, ObjectEntity object_entity, String thread_name,
            String parent_name, String group_name, ThreadEntity parent,
            boolean authoritative) {
        super(id, true, authoritative);
        this.object_entity = object_entity;
        this.thread_name = thread_name;
        this.parent_name = parent_name;
        this.group_name = group_name;
        this.parent = parent;
    }

    public String getThreadName() {
        return this.thread_name;
    }
    
    void setThreadName(String name) {
        this.thread_name = name;
    }
    
    public String getParentName() {
        return this.parent_name;
    }
    
    void setParentName(String name) {
        this.parent_name = name;
    }
    
    public String getGroupName() {
        return this.group_name;
    }
    
    void setGroupName(String name) {
        this.group_name = name;
    }
    
    public ThreadEntity getParent() {
        return this.parent;
    }
    
    void setParent(ThreadEntity parent) {
        this.parent = parent;
    }

    public ObjectEntity getObjectEntity() {
        return this.object_entity;
    }

    void setObjectEntity(ObjectEntity object_entity) {
        this.object_entity = object_entity;
    }
    
    public static int registerFlag() {
        return ThreadEntity.storage_factory.registerFlag();
    }
    
    public static int registerCounter() {
        return ThreadEntity.storage_factory.registerCounter();
    }
    
    public static int registerStorageSpace() {
        return ThreadEntity.storage_factory.registerStorageSpace();
    }
    
    public StorageFactory getStorageFactory() {
        return ThreadEntity.storage_factory;
    }
}
