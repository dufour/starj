package starj.coffer;

import starj.*;

public abstract class Instruction implements Storage {
    private static StorageFactory storage_factory = new StorageFactory();
    private Storage storage;
    private short opcode;
    private int offset;
    private int length;
    private Instruction prev;
    private Instruction next;

    public Instruction(short opcode, int length, int offset) {
        this.opcode = opcode;
        this.length = length;
        this.offset = offset;
        this.next = null;
        this.prev = null;
    }

    public int getOffset() {
        return this.offset;
    }
    
    public short getOpcode() {
        return this.opcode;
    }

    public int hashCode() {
        return this.offset;
    }
    
    public int getLength() {
        return this.length;
    }

    public Instruction getNext() {
        return this.next;
    }

    public Instruction getPrev() {
        return this.prev;
    }

    void setNext(Instruction inst) {
        this.next = inst;
    }

    void setPrev(Instruction inst) {
        this.prev = inst;
    }

    public abstract String getOpcodeName();

    public String toString() {
        return this.offset + " " + this.getOpcodeName();
    }
    
    // Storage interface
    
    private Storage getStorage() {
        Storage rv = this.storage;
        if (rv == null) {
            rv = Instruction.storage_factory.newStorage();
            this.storage = rv;
        }
        
        return rv;
    }
    
    public static StorageFactory getStorageFactory() {
        return Instruction.storage_factory;
    }
    
    public static int registerFlag() {
        return Instruction.storage_factory.registerFlag();
    }
    
    public static int registerCounter() {
        return Instruction.storage_factory.registerCounter();
    }
    
    public static int registerStorageSpace() {
        return Instruction.storage_factory.registerStorageSpace();
    }
    
    /*
    public int registerFlag() {
        return Instruction.storage_factory.registerFlag();
    }
    
    public int registerCounter() {
        return Instruction.storage_factory.registerCounter();
    }
    
    public int registerStorageSpace() {
        return Instruction.storage_factory.registerStorageSpace();
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
