package starj.toolkits.services;

import starj.Operation;

/**
 * Service operations only provide their services to
 * subsequent operations in the event pipe. A service operation
 * can be safely disabled when no other operation depends on it.
 */
public interface Service extends Operation {
    
}
