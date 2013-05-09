package starj.toolkits.services;

import starj.coffer.InvokeInstruction;

public interface Propagation {
    public void propagate(InvokeInstruction call_site,
            MethodEntity new_method, ExecutionContext call_context);
    public void unpropagate(ExecutionContext context);
}
