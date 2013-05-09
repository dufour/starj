package starj;

public abstract class RecursiveHierarchyVisitor
        implements HierarchyVisitor {
    private boolean visit_disabled;

    public RecursiveHierarchyVisitor() {
        this(false);
    }
            
    public RecursiveHierarchyVisitor(boolean visit_disabled) {
        this.visit_disabled = visit_disabled;
    }

    public final void visit(HierarchyElement element) {
        if (!(element.isEnabled() || this.visit_disabled)) {
            return;
        }
        this.visitElement(element);
    }

    public void visitElement(HierarchyElement element) {
        if (element instanceof Container) {
            this.visitContainer((Container) element);
        }
    }

    public void visitContainer(Container container) {
        for (HierarchyElementIterator i = container.iterator(); i.hasNext(); ) {
            this.visit(i.next());
        }
    }
    
    public void init() {
        // Intentionally empty
    }
    
    public void done() {
        // Intentionally empty
    }
}
