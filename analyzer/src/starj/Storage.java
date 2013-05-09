package starj;

public interface Storage {
    /*
    public int registerFlag();
    public int registerCounter();
    public int registerStorageSpace();
    */
    public boolean getFlag(int storage_id);
    public void setFlag(int storage_id, boolean value);
    public long getCounter(int storage_id);
    public void setCounter(int storage_id, long value);
    public void stepCounter(int storage_id);
    public void decrCounter(int storage_id);
    public void addToCounter(int storage_id, long delta);
    public Object getStorageSpace(int storage_id);
    public void setStorageSpace(int storage_id, Object value);
}
