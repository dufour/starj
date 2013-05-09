package starj.toolkits.printers;

import java.io.PrintStream;
import java.util.*;

import starj.*;
import starj.coffer.InvokeInstruction;
import starj.dependencies.OperationSet;
import starj.options.*;
import starj.toolkits.services.*;

public class CallGraphPrinter extends AbstractPrinter implements Propagation {
    private boolean use_dot = false;
    private Map edges;

    public CallGraphPrinter(String name, String description) {
        super(name, description);
        PropagationManager.v().addPropagation(this);
    }
    
    public CallGraphPrinter(String name, String description, PrintStream out) {
        super(name, description, out);
        PropagationManager.v().addPropagation(this);
    }
    
    public OperationSet operationDependencies() {
        OperationSet ops = super.operationDependencies();
        ops.add(PropagationManager.v());
        return ops;
    }
    
    public void init() {
        super.init();
        this.edges = new HashMap();
    }
    
    public void apply(EventBox box) {
        // Intentionally empty
    }
    
    public void propagate(InvokeInstruction call_site,
            MethodEntity new_method, ExecutionContext call_context) {
        MethodEntity current_method = call_context.getMethod();
        if (current_method != null) {
            Set targets = (Set) this.edges.get(current_method);
            if (targets == null) {
                targets = new HashSet();
                this.edges.put(current_method, targets);
            }
            
            if (!this.edges.containsKey(new_method)) {
                this.edges.put(new_method, null); // Insert placeholder
            }
            
            targets.add(new_method);
        }
    }
    
    public void unpropagate(ExecutionContext context) {
        // Intentionally empty
    }
    
    public void configure(ElementConfigArgument config, Object value) {
        String name = config.getName();
        if (name.equals("dot")) {
            if (value != null) {
                this.use_dot = ((Boolean) value).booleanValue();
            }
        } else {
            super.configure(config, value);
        }
    }

    public ElementConfigSet getConfigurationSet() {
        ElementConfigSet set = super.getConfigurationSet();
        ElementConfigArgument dot_arg = new ElementConfigArgument(
                0,
                "dot",
                "Specifies whether the 'dot' output format will be used",
                "Specifies whether the 'dot' output format will be used to "
                        + "print the call graph",
                false
        );
        dot_arg.addArgument(new BooleanArgument(
                0,
                "value",
                true,  // required
                false, // not repeatable
                "boolean",
                "true or false"
        ));
        set.addConfig(dot_arg);
        return set;
    }

    public void done() {
        PrintStream out = this.out;
        if (this.use_dot) {
            out.println("digraph CallGraph {");
            
            // Output node descriptions
            out.println("  /* Nodes */");
            out.println("  node [shape=box];");
            for (Iterator i = this.edges.keySet().iterator(); i.hasNext(); ) {
                MethodEntity src = (MethodEntity) i.next();
                out.println("  " + src.getID() + " [label=\""
                        + src.getClassEntity().getClassName() + "\\n"
                        + src.getMethodName() + "\"];");
            }
            
            // Output edge descriptions
            out.println("\n  /* Edges */");
            for (Iterator i = this.edges.keySet().iterator(); i.hasNext(); ) {
                MethodEntity src = (MethodEntity) i.next();
                Set targets = (Set) this.edges.get(src);
                if (targets == null) {
                    continue;
                }
                for (Iterator j = targets.iterator(); j.hasNext(); ) {
                    MethodEntity tgt = (MethodEntity) j.next();
                    out.println("  " + src.getID() + " -> " + tgt.getID()
                            + ";");
                }
            }
            out.println("}");
        } else {
            for(Iterator i = this.edges.keySet().iterator(); i.hasNext(); ) {
                MethodEntity src = (MethodEntity) i.next();
                Set targets = (Set) this.edges.get(src);
                if (targets == null) {
                    continue;
                }
                for(Iterator j = targets.iterator(); j.hasNext(); ) {
                    MethodEntity tgt = (MethodEntity) j.next();
                    out.println(src + " -> " + tgt);
                }
            }
        }
    }


}
