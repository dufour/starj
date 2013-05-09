package starj.options;

public interface StringQueue {
    public String add(String arg);
    public String get();
    public String push(String arg);
    public String peek();
    public int size();
    public boolean isEmpty();
    public void clear ();
    public String toString();
}
