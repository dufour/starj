package starj.util.xml;

import java.util.*;

public class XMLAttributes {
    private List attribs;
    
    public XMLAttributes() {
        this.attribs = new LinkedList();
    }

    public XMLAttributes(XMLAttribute attrib) {
        this();
        this.attribs.add(attrib);
    }

    public XMLAttributes(XMLAttribute[] attribs) {
        this();
        if (attribs != null) {
            for (int i = 0; i < attribs.length; i++) {
                this.attribs.add(attribs[i]);
            }
        }
    }
    
    public void add(XMLAttribute attrib) {
        this.attribs.add(attrib);
    }

    public XMLAttribute[] toArray() {
        XMLAttribute[] rv = new XMLAttribute[this.attribs.size()];
        rv = (XMLAttribute[]) this.attribs.toArray(rv);
        return rv;
    }
}
