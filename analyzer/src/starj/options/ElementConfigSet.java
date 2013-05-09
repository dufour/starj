package starj.options;

import java.util.*;

public class ElementConfigSet {
    private Map config_name_map;
    
    public ElementConfigSet() {
        this.config_name_map = new HashMap();
    }

    public void addConfig(ElementConfigArgument argument) {
        this.config_name_map.put(argument.getName(), argument);
    }
    
    public ArgumentContainer toArgumentContainer() {
        return new ElementConfigSetContainer(this);
    }
    
    public String[] getConfigurationNames() {
        Collection c = this.config_name_map.keySet();
        String[] rv = new String[c.size()];
        return (String[]) c.toArray(rv);
    }
    
    public ElementConfigArgument[] getArguments() {
        Collection c = this.config_name_map.values();
        ElementConfigArgument[] rv = new ElementConfigArgument[c.size()];
        return (ElementConfigArgument[]) c.toArray(rv);
    }
    
    private class ElementConfigSetContainer implements ArgumentContainer {
        private List arguments;
        
        ElementConfigSetContainer(ElementConfigSet set) {
            this.arguments = new LinkedList(set.config_name_map.values());
        }
        
        public int getArgumentCount() {
            return this.arguments.size();
        }
        
        public ArgumentIterator iterator() {
            return new ElementConfigSetIterator(this);
        }
        
        private class ElementConfigSetIterator implements ArgumentIterator {
            private Iterator iterator;
            
            public ElementConfigSetIterator(ElementConfigSetContainer container) {
                this.iterator = container.arguments.iterator();
            }
            
            public Argument next() {
                return (Argument) this.iterator.next();
            }
            
            public boolean hasNext() {
                return this.iterator.hasNext();
            }
        }
    }
}
