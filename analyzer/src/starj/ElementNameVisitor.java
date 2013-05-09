package starj;

import java.util.*;

public abstract class ElementNameVisitor extends RecursiveHierarchyVisitor {
        private LinkedList container_names;

        public ElementNameVisitor() {
            this(false);
        }

        public ElementNameVisitor(boolean visit_disabled) {
            super(visit_disabled);
            this.container_names = new LinkedList();
        }

        private String getFullName(String name) {
            String rv = "";
            for (Iterator i = this.container_names.iterator(); i.hasNext(); ) {
                rv  += i.next() + ".";
            }

            rv += name;

            return rv;
        }

        public void visitElement(HierarchyElement element) {
            if (!(element instanceof RootPack)) {
                this.visitElement(element, this.getFullName(element.getName()));
            }
            super.visitElement(element);
        }

        public abstract void visitElement(HierarchyElement element,
                String name);

        public void visitContainer(Container container) {
            if (container instanceof RootPack) {
                super.visitContainer(container);
            } else {
                this.container_names.add(container.getName());
                super.visitContainer(container);
                this.container_names.removeLast();
            }
        }
    }
