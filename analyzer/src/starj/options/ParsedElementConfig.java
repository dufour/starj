package starj.options;

import starj.HierarchyElement;

public class ParsedElementConfig extends ParsedResult {
    private HierarchyElement element;
    
    public ParsedElementConfig(HierarchyElement element) {
        super();
        this.element = element;
    }

    public HierarchyElement getElement() {
        return this.element;
    }
}
