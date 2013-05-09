package starj.coffer;

import java.io.*;
import java.util.*;

public class Attributes {
    private Attribute[] attributes;

    public Attributes(DataInput input, ConstantPool cp,
            AttributeFactory factory) {
        try {
            int attribute_count = input.readUnsignedShort();
            Attribute[] attributes = new Attribute[attribute_count];
            for (int i = 0; i < attribute_count; i++) {
                attributes[i] = factory.parse(input, cp);
            }
            this.attributes = attributes;
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }
    
    public Attributes(Attribute[] attributes) {
        if (attributes != null) {
            this.attributes = attributes;
        } else {
            this.attributes = new Attribute[0];
        }
    }
    
    public Attribute[] getAttributes() {
        return (Attribute[]) this.attributes.clone();
    }

    public Attribute getAttribute(int index) {
        return this.attributes[index];
    }
    
    public Attribute lookupFirst(String name) {
        return this.lookupFirst(name, 0);
    }
    
    public Attribute lookupFirst(String name, int start_index) {
        for (int i = start_index; i < this.attributes.length; i++) {
            Attribute attrib = this.attributes[i];
            if (attrib.getName().equals(name)) {
                return attrib;
            }
        }
        
        return null;
    }
    
    public Attributes lookupAll(String name) {
        return this.lookupAll(name, 0);
    }
    
    public Attributes lookupAll(String name, int start_index) {
        List l = new LinkedList();
        for (int i = start_index; i < this.attributes.length; i++) {
            Attribute attrib = this.attributes[i];
            if (attrib.getName().equals(name)) {
                l.add(attrib);
            }
        }
        
        Attribute[] attribs = new Attribute[l.size()];
        attribs = (Attribute[]) l.toArray(attribs);
        return new Attributes(attribs);
    }

    public int size() {
        return this.attributes.length;
    }
}
