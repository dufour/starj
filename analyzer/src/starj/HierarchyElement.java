package starj;

import starj.options.ElementConfigSet;
import starj.options.ElementConfigArgument;

public interface HierarchyElement {
    public String getName();
    public String getDescription();
    public void accept(HierarchyVisitor visitor);
    
    public void disable();
    public void enable();
    public boolean isEnabled();
    public void setEnabled(boolean enabled);

    public void configure(ElementConfigArgument config, Object value);
    public ElementConfigSet getConfigurationSet();
}
