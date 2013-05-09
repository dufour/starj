package starj.toolkits.services;

import starj.*;

public abstract class Entity implements Storage {
    private Storage storage;
    private int id;
    private int authoritative_count;
    private boolean is_std_lib;
    
    public Entity(int id, boolean standard_lib) {
        this(id, standard_lib, true);
    }

    public Entity(int id, boolean standard_lib, boolean authoritative) {
        this.id = id;
        this.is_std_lib = standard_lib;
        this.authoritative_count = (authoritative ? 1 : 0);
    }

    public int getID() {
        return this.id;
    }

    protected void setID(int id) {
        this.id = id;
    }

    public boolean isAuthoritative() {
        return this.authoritative_count > 0;
    }

    public int getAuthoritativeCount() {
        return this.authoritative_count;
    }

    public void stepAuthoritativeCount() {
        this.authoritative_count += 1;
    }

    public void setAuthoritativeCount(int authoritative_count) {
        this.authoritative_count = authoritative_count;
    }

    public void setStandardLib(boolean standard_lib) {
        this.is_std_lib = standard_lib;
    }

    public boolean isStandardLibrary() {
        return this.is_std_lib;
    }
    
    // Storage interface
    
    public abstract StorageFactory getStorageFactory();
    
    private Storage getStorage() {
        Storage rv = this.storage;
        if (rv == null) {
            rv = this.getStorageFactory().newStorage();
            this.storage = rv;
        }
        
        return rv;
    }
    
    /*
    public static int registerFlag() {
        return Instruction.storage_factory.registerFlag();
    }
    
    public static int registerCounter() {
        return Instruction.storage_factory.registerCounter();
    }
    
    public static int registerStorageSpace() {
        return Instruction.storage_factory.registerStorageSpace();
    }
    */
    /*
    public int registerFlag() {
        return this.getStorageFactory().registerFlag();
    }
    
    public int registerCounter() {
        return this.getStorageFactory().registerCounter();
    }
    
    public int registerStorageSpace() {
        return this.getStorageFactory().registerStorageSpace();
    }
    */
    
    public boolean getFlag(int storage_id) {
        return this.getStorage().getFlag(storage_id);
    }
    
    public void setFlag(int storage_id, boolean value) {
        this.getStorage().setFlag(storage_id, value);
    }
    
    public long getCounter(int storage_id) {
        return this.getStorage().getCounter(storage_id);
    }
    
    public void setCounter(int storage_id, long value) {
        this.getStorage().setCounter(storage_id, value);
    }
    
    public void stepCounter(int storage_id) {
        this.getStorage().stepCounter(storage_id);
    }
    
    public void decrCounter(int storage_id) {
        this.getStorage().decrCounter(storage_id);
    }
    
    public void addToCounter(int storage_id, long delta) {
        this.getStorage().addToCounter(storage_id, delta);
    }
    
    public Object getStorageSpace(int storage_id) {
        return this.getStorage().getStorageSpace(storage_id);
    }
    
    public void setStorageSpace(int storage_id, Object value) {
        this.getStorage().setStorageSpace(storage_id, value);
    }
}
