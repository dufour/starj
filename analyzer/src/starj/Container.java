package starj;

public interface Container extends HierarchyElement {
    public void add(HierarchyElement element);
    public HierarchyElementIterator iterator();
    public HierarchyElement getByName(String name);
    public void reset();
}
