package starj.dependencies;

import java.util.*;
import java.io.PrintStream;

import starj.*;
import starj.toolkits.services.Service;
import starj.io.logging.LogManager;

public class DependencyGraph {
    private Map contents; // Operation -> DependencyGraphNode
    private Set reachable_ops; // {Operation}
    private static NodeConnector default_connector = new NodeConnector() {
        public void connect(DependencyGraphNode n, DependencyGraphNode dep) {
            n.add_edge(dep);
        }
    };

    public DependencyGraph() {
        this.clear();
    }

    public DependencyGraph(Container container) {
        this(container, false);
    }

    public DependencyGraph(Container container, boolean visit_unreachable) {
        this.clear();
        (new OperationVisitor(visit_unreachable) {
            public void visitOperation(Operation operation) {
                DependencyGraph.this.add(operation);
                DependencyGraph.this.reachable_ops.add(operation);
            }
        }).visit(container);
    }

    private boolean isEnabled(Operation operation) {
        return (this.reachable_ops.contains(operation)
                && operation.isEnabled());
    }

    private boolean add(Operation operation, NodeConnector connector) {
        if (!this.contents.containsKey(operation)) {
            DependencyGraphNode node = new DependencyGraphNode(operation);
            this.contents.put(operation, node);

            // Recursively add all dependencies and create proper edges
            OperationSet deps = operation.operationDependencies();
            for (OperationIterator i = deps.iterator(); i.hasNext(); ) {
                Operation dep = i.next();
                this.add(dep, connector);
                DependencyGraphNode dep_node
                        = (DependencyGraphNode) this.contents.get(dep);
                connector.connect(node, dep_node);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean add(Operation operation) {
        return this.add(operation, DependencyGraph.default_connector);
    }

    public void clear() {
        this.contents = new HashMap();
        this.reachable_ops = new HashSet();
    }

    private List dfs() {
        ListAccumulator accumulator = new ListAccumulator();
        this.dfs(this.contents.values().iterator(), accumulator);
        return accumulator.getList();
    }

    private void dfs(Iterator order, NodeAccumulator accumulator) {
        // Perform a depth-first search
        for (Iterator i = this.contents.values().iterator(); i.hasNext(); ) {
            DependencyGraphNode node = (DependencyGraphNode) i.next();
            node.setColour(DependencyGraphNode.WHITE);
        }

        while (order.hasNext()) {
            DependencyGraphNode node = (DependencyGraphNode) order.next();
            if (node.getColour() == DependencyGraphNode.WHITE) {
                this.dfs_visit(node, accumulator);
            }
        }
    }

    private void dfs_visit(DependencyGraphNode node,
            NodeAccumulator accumulator) {
        node.setColour(DependencyGraphNode.GRAY);
        for (Iterator i = node.neighbours(); i.hasNext(); ) {
            DependencyGraphNode neighbour = (DependencyGraphNode) i.next();
            if (neighbour.getColour() == DependencyGraphNode.WHITE) {
                this.dfs_visit(neighbour, accumulator);
            }
        }
        node.setColour(DependencyGraphNode.BLACK);
        accumulator.accumulate(node);
    }

    public DependencyGraph transpose() {
        NodeConnector reverse_connector = new NodeConnector() {
            public void connect(DependencyGraphNode n,
                    DependencyGraphNode dep) {
                dep.add_edge(n);
            }
        };
        DependencyGraph result = new DependencyGraph();
        for (Iterator i = this.contents.values().iterator(); i.hasNext(); ) {
            DependencyGraphNode node = (DependencyGraphNode) i.next();
            result.add(node.getOperation(), reverse_connector);
        }

        return result;
    }

    private List getSCCs() {
        List sccs = new LinkedList();
        List order = this.dfs();
        DependencyGraph t = this.transpose();
        for (Iterator i = order.iterator(); i.hasNext(); ) {
            DependencyGraphNode orig_node = (DependencyGraphNode) i.next();
            Operation op = orig_node.getOperation();
            DependencyGraphNode new_node
                    = (DependencyGraphNode) t.contents.get(op);
            if (new_node.getColour() == DependencyGraphNode.WHITE) {
                // This is a new SCC
                OperationSetAccumulator accumulator
                        = new OperationSetAccumulator();
                this.dfs_visit(new_node, accumulator);
                // Insert this SCC at the beginning of the list, so
                // that the result will be in reverse topological order
                // (with respect to the original graph). This is what we
                // need since we want to process the dependencies first.
                sccs.add(0, accumulator.getOperationSet());
            }
        }

        return sccs;
    }

    public void checkDependencies() {
        List sccs = this.getSCCs();
        String missing_dep_name = null;
        // For each SCC, in reverse topological order
        for (Iterator i = sccs.iterator(); i.hasNext(); ) {
            OperationSet scc = (OperationSet) i.next();
            boolean disable_scc = false;
scc_loop:
            for (OperationIterator j = scc.iterator(); j.hasNext(); ) {
                Operation operation = j.next();
                if (this.isEnabled(operation)) {
                    // Check for a disabled dependency
                    DependencyGraphNode n = (DependencyGraphNode)
                            this.contents.get(operation);
                    for (Iterator k = n.neighbours(); k.hasNext(); ) {
                        DependencyGraphNode dep
                                = (DependencyGraphNode) k.next();
                        if (!this.isEnabled(dep.getOperation())) {
                            disable_scc = true;
                            missing_dep_name = dep.getOperation().getName();
                            break scc_loop;
                        }
                    }
                } else {
                    disable_scc = true;
                    missing_dep_name = operation.getName();
                    break scc_loop;
                }
            }

            if (disable_scc) {
                for (OperationIterator j = scc.iterator(); j.hasNext(); ) {
                    Operation operation = j.next();
                    if (this.isEnabled(operation)) {
                        LogManager.v().logMessage("Disabling: "
                                + operation.getName()
                                + " (missing dependency: " + missing_dep_name
                                + ")");
                        operation.disable();
                    }
                }
            }
        }

        /* Look for unnecessary services */
        
        Collections.reverse(sccs);
        DependencyGraph t = this.transpose();

        // For each SCC, in topological order
        for (Iterator i = sccs.iterator(); i.hasNext(); ) {
            OperationSet scc = (OperationSet) i.next();
            for (OperationIterator j = scc.iterator(); j.hasNext(); ) {
                Operation op = j.next();
                if (this.isEnabled(op) && op instanceof Service) {
                    DependencyGraphNode n
                            = (DependencyGraphNode) t.contents.get(op);
                    boolean disable_service = true;
                    for (Iterator k = n.neighbours(); k.hasNext(); ) {
                        DependencyGraphNode neighbour_node
                                = (DependencyGraphNode) k.next();
                        Operation neighbour = neighbour_node.getOperation();
                        if (this.isEnabled(neighbour)) {
                            disable_service = false;
                            break;
                        }
                    }
                    if (disable_service) {
                        LogManager.v().logMessage(
                                "Disabling unnecessary service: " 
                                + op.getName());
                        op.disable();
                    }
                }
            }
        }
    }

    public void toDot(PrintStream out) {
        Set defined_set = new HashSet();
        out.println("digraph DependencyGraph {\n");
        for (Iterator i = this.contents.values().iterator(); i.hasNext(); ) {
            DependencyGraphNode n = (DependencyGraphNode) i.next();
            this.toDot(out, n, defined_set);
            
        }
        out.println("}");
    }

    private void toDot(PrintStream out, DependencyGraphNode n, Set defined) {
        Operation op = n.getOperation();
        if (!defined.contains(op)) {
            String op_name = op.getName();
            out.println("  " + op_name + "[shape=box,label=\""
                    + (op.isEnabled() ? op_name : "(" + op_name + ")")
                    + "\"]");
            defined.add(op);

            for (Iterator j = n.neighbours(); j.hasNext(); ) {
                DependencyGraphNode neighbour = (DependencyGraphNode) j.next();
                this.toDot(out, neighbour, defined);
                out.println("  " + op_name + " -> "
                        + neighbour.getOperation().getName());
            }
        }
    }

    private class DependencyGraphNode {
        static final byte WHITE = 0;
        static final byte GRAY  = 1;
        static final byte BLACK = 2;

        private Operation operation;
        private byte colour;
        private Set neighbours;

        DependencyGraphNode(Operation operation) {
            this.operation = operation;
            this.colour = DependencyGraphNode.WHITE;
            this.neighbours = new HashSet();
        }

        public Operation getOperation() {
            return this.operation;
        }

        public boolean add_edge(DependencyGraphNode node) {
            return this.neighbours.add(node);
        }

        public Iterator neighbours() {
            return this.neighbours.iterator();
        }

        public byte getColour() {
            return this.colour;
        }

        public void setColour(byte colour) {
            this.colour = colour;
        }
    }

    private interface NodeConnector {
        public void connect(DependencyGraphNode n, DependencyGraphNode dep);
    }

    private interface NodeAccumulator {
        public void accumulate(DependencyGraphNode node);
    }

    private class ListAccumulator implements NodeAccumulator {
        private List list;

        ListAccumulator() {
            this.list = new LinkedList();
        }

        ListAccumulator(List list) {
            this.list = list;
        }

        public void accumulate(DependencyGraphNode node) {
            this.list.add(0, node);
        }

        public List getList() {
            return this.list;
        }
    }

    private class OperationSetAccumulator implements NodeAccumulator {
        private OperationSet set;

        OperationSetAccumulator() {
            this.set = new OperationSet();
        }

        OperationSetAccumulator(OperationSet set) {
            this.set = set;
        }

        public void accumulate(DependencyGraphNode node) {
            this.set.add(node.getOperation());
        }

        public OperationSet getOperationSet() {
            return this.set;
        }
    }
}
