package starj.toolkits.services;

import starj.ApplicationMaster;
import starj.StorageFactory;
import starj.coffer.types.Type;


public class ObjectEntity extends TypedEntity {
    private static StorageFactory storage_factory = new StorageFactory();
    private ArenaEntity arena;
    private int size;

    public ObjectEntity(int id, ArenaEntity arena, Type type, int size) {
        this(id, arena, type, size, true);
    }

    public ObjectEntity(int id, ArenaEntity arena, Type type, int size,
            boolean authoritative) {
        super(id, ApplicationMaster.v().isStandardLibrary(type), type,
                authoritative);
        this.arena = arena;
        this.size = size;
    }

    public void moveTo(int id, ArenaEntity arena) {
        if (this.arena != null) {
            this.arena.remove(this);
        }
        this.arena = arena;
        this.setID(id);
        if (arena != null) {
            arena.add(this);
        }
    }

    public ArenaEntity getArena() {
        return this.arena;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public static int registerFlag() {
        return ObjectEntity.storage_factory.registerFlag();
    }
    
    public static int registerCounter() {
        return ObjectEntity.storage_factory.registerCounter();
    }
    
    public static int registerStorageSpace() {
        return ObjectEntity.storage_factory.registerStorageSpace();
    }
    
    public StorageFactory getStorageFactory() {
        return ObjectEntity.storage_factory;
    }
}
