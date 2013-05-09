package starj;

import java.io.PrintStream;
import starj.util.StringUtils;

public class RootPack extends Pack {
    RootPack() {
        super("<Root>", "Represents the root of the element hierarchy");
    }

    public void displayHierarchy(PrintStream out) {
        this.accept(new HierarchyDisplayVisitor(out));
    }

    private class HierarchyDisplayVisitor extends OperationVisitor {
        public static final int INDENT_STEP = 3;
        private int indent_level;
        private PrintStream out;
        
        HierarchyDisplayVisitor(PrintStream out) {
            super(true); // Visit disabled elements
            this.indent_level = 0;
            this.out = out;
        }
        
        public void visitElement(HierarchyElement element) {
            if (element instanceof RootPack) {
                this.visitRootPack((RootPack) element);
            } else {
                super.visitElement(element);
            }
        }

        public void visitRootPack(RootPack root_pack) {
            this.out.println(root_pack.getName());
            this.indent_level += INDENT_STEP;
            super.visitContainer(root_pack);
            this.indent_level -= INDENT_STEP;
        }

        public void visitOperation(Operation operation) {
            this.out.println(StringUtils.indent("o "
                        + this.getElementName(operation), this.indent_level));
        }

        public void visitContainer(Container container) {
            this.out.println(StringUtils.indent("+ "
                        + this.getElementName(container), this.indent_level));
            this.indent_level += INDENT_STEP;
            super.visitContainer(container);
            this.indent_level -= INDENT_STEP;
        }

        private String getElementName(HierarchyElement element) {
            String name = element.getName();
            if (!element.isEnabled()) {
                name = "(" + name + ")";
            }

            return name;
        }
    }
}
