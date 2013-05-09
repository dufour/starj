package starj;

public abstract class OperationVisitor extends RecursiveHierarchyVisitor {
    public OperationVisitor() {
        super();
    }
            
    public OperationVisitor(boolean visit_disabled) {
        super(visit_disabled);
    }

    public void visitElement(HierarchyElement element) {
        if (element instanceof Operation) {
            this.visitOperation((Operation) element);
        } else {
            super.visitElement(element);
        }
    }

    public abstract void visitOperation(Operation operation);
}
