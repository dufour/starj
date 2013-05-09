package starj;

import starj.options.*;

public abstract class AbstractHierarchyElement implements HierarchyElement {
    private String name;
    private String description;
    private boolean enabled;
    
    public AbstractHierarchyElement(String name, String description) {
        this.name = name;
        this.description = description;
        this.enabled = true;
    }

    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void accept(HierarchyVisitor visitor) {
        visitor.visit(this);
    }
    
    public void configure(ElementConfigArgument config, Object value) {
        String name = config.getName();
        if (name.equals("enabled")) {
            if (value != null) {
                this.setEnabled(((Boolean) value).booleanValue());
            }
        } else {
            throw new RuntimeException("Invalid configuration name: '" 
                    + name + "'");
        }
    }
    
    public ElementConfigSet getConfigurationSet() {
        ElementConfigSet set = new ElementConfigSet();
        ElementConfigArgument enabled_arg = new ElementConfigArgument(
                0,
                "enabled",
                "Enables/disables this element",
                "Configures whether this element is enabled or not",
                false
        );
        enabled_arg.addArgument(new BooleanArgument(
                0,
                "value",
                "Boolean",
                "Boolean"
        ));
        set.addConfig(enabled_arg);
        return set;
    }
}
