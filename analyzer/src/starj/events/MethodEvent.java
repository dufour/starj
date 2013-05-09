package starj.events;

public interface MethodEvent extends JVMPIEvent {
    public int getMethodID();
    public void setMethodID(int method_id);
}
