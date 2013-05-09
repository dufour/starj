package starj.toolkits.services;

import java.util.*;

import starj.StorageFactory;

public class ArenaEntity extends Entity {
    private static StorageFactory storage_factory = new StorageFactory();
    private String name;
    private Set objects;
    
    public ArenaEntity(int id, String name) {
        this(id, name, true);
    }
    
    public ArenaEntity(int id, String name, boolean authoritative) {
        super(id, true, authoritative);
        this.name = name;
        this.objects = new HashSet();
    }

    public String getName() {
        return this.name;
    }

    public boolean remove(ObjectEntity obj_entity) {
        return this.objects.remove(obj_entity);
    }

    public boolean add(ObjectEntity obj_entity) {
        return this.objects.add(obj_entity);
    }

    public Set getObjects() {
        return Collections.unmodifiableSet(this.objects);
    }
    
    public static int registerFlag() {
        return ArenaEntity.storage_factory.registerFlag();
    }
    
    public static int registerCounter() {
        return ArenaEntity.storage_factory.registerCounter();
    }
    
    public static int registerStorageSpace() {
        return ArenaEntity.storage_factory.registerStorageSpace();
    }
    
    public StorageFactory getStorageFactory() {
        return ArenaEntity.storage_factory;
    }
}
