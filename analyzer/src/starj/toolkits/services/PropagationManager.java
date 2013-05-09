package starj.toolkits.services;

import java.util.*;

import starj.HierarchyElement;
import starj.coffer.InvokeInstruction;
import starj.util.*;

public class PropagationManager extends PropagationOperation {
    /* NOTE: The PropagationManager class cannot implement the Service
     * interface because it would mean that it would be disabled whenever no
     * operation depends on it. However, it can be safely turned off when it
     * does not have any associated propagations. So, it overrides the
     * isEnabled() method to achieve a similar effect: it will only be enabled
     * if it has not been manually disabled _and_ has associated propagations.
     *
     * This solution has potential side effects:
     *   1- Anybody querying the status of a PropagationManager instance can get
     *      a false negative value.
     *   2- All propagatations must be added to the PropagationManager _before_
     *      its status is checked. Otherwise, it might be disabled on account
     *      of having no work to perform.
     */
    
    private static PropagationManager instance = new PropagationManager();
    private List propagations;
    private Propagation[] propagation_array;

    private PropagationManager() {
        super("PropagationManager",
                "Manages custom propagation implementations");
        this.propagations = new LinkedList();
    }
    
    public boolean isEnabled() {
        // Enabled only if not disabled manually and has propagations to perform
        return super.isEnabled() && !this.propagations.isEmpty();
    }
    
    public void init() {
        Collection enabled_propagations = CollectionUtils.filter(
                this.propagations, new Filter() {
                    public boolean keep(Object obj) {
                        if (obj instanceof HierarchyElement) {
                            return ((HierarchyElement) obj).isEnabled();
                        } else {
                            return true;
                        }
                    }
                });
        this.propagation_array = new Propagation[enabled_propagations.size()];
        this.propagation_array = (Propagation[])
                enabled_propagations.toArray(this.propagation_array);
    }
 
    public void addPropagation(Propagation propagation) {
        this.propagations.add(propagation);
    }

    public void propagate(InvokeInstruction call_site,
            MethodEntity new_method, ExecutionContext call_context) {
        if (this.propagations != null) {
            Propagation[] propagations = this.propagation_array;
            for (int i = 0; i < propagations.length; i++) {
                propagations[i].propagate(call_site,
                        new_method, call_context);
            }
        }
    }
    
    public void unpropagate(ExecutionContext context) {
        if (this.propagations != null) {
            Propagation[] propagations = this.propagation_array;
            for (int i = 0; i < propagations.length; i++) {
                propagations[i].unpropagate(context);
            }
        }
    }
    
    public void done() {
        this.propagation_array = null;
    }
    
    public static PropagationManager v() {
        return PropagationManager.instance;
    }
}
