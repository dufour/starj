package starj;

public interface HierarchyVisitor {
    public void init();
    public void visit(HierarchyElement element);
    public void done();
}
