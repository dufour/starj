package starj.events;

public interface ObjectEvent extends JVMPIEvent {
    public int getObjectID();
    public void setObjectID(int obj_id);
}

