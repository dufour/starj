package starj.spec;

import java.util.*;

class AST {
    private ASTEventDeclarationSequence events;
    
    public AST(ASTEventDeclarationSequence events) {
        this.events = events;
    }

    public ASTEventDeclarationSequence getEvents() {
        return this.events;
    }

    public String toString() {
        return String.valueOf(this.events);
    }
}

abstract class ASTNode {

}

class ASTEventDeclarationSequence extends ASTNode {
    private List events;
    
    public ASTEventDeclarationSequence() {
        this.events = new LinkedList();
    }

    public ASTEventDeclarationSequence(ASTEventDeclaration event) {
        this();
        this.add(event);
    }

    public void add(ASTEventDeclaration event) {
        this.events.add(0, event);
    }

    public ASTEventDeclaration[] getEvents() {
        ASTEventDeclaration[] rv = new ASTEventDeclaration[this.events.size()];
        rv = (ASTEventDeclaration[]) this.events.toArray(rv);
        return rv;
    }

    public String toString() {
        String rv = "";
        for (Iterator i = this.events.iterator(); i.hasNext(); ) {
            rv += i.next() + "\n";
        }

        return rv;
    }
}

abstract class ASTEventDeclaration extends ASTNode {
    private ASTFieldDeclarationSequence fields;

    public ASTEventDeclaration(ASTFieldDeclarationSequence fields) {
        this.fields = fields;
    }

    public ASTFieldDeclarationSequence getFields() {
        return this.fields;
    }

    public String toString() {
        return "{\n" + this.fields + "\n}\n";
    }
}

class ASTDefaultEventDeclaration extends ASTEventDeclaration {
    public ASTDefaultEventDeclaration(ASTFieldDeclarationSequence fields) {
        super(fields);
    }

    public String toString() {
        return "default " + super.toString();
    }
}

class ASTPatternEventDeclaration extends ASTEventDeclaration {
    private ASTPattern pattern;
    
    public ASTPatternEventDeclaration(ASTPattern pattern,
            ASTFieldDeclarationSequence fields) {
        super(fields);
        this.pattern = pattern;
    }

    public ASTPattern getPattern() {
        return this.pattern;
    }

    public String toString() {
        return "event " + this.pattern + " " + super.toString();
    }
}

class ASTFieldDeclarationSequence extends ASTNode {
    private List fields;

    public ASTFieldDeclarationSequence() {
        this.fields = new LinkedList();
    }

    public ASTFieldDeclarationSequence(ASTFieldDeclaration field) {
        this();
        this.add(field);
    }

    public void add(ASTFieldDeclaration field) {
        this.fields.add(0, field);
    }

    public ASTFieldDeclaration[] getFields() {
        ASTFieldDeclaration[] rv = new ASTFieldDeclaration[this.fields.size()];
        rv = (ASTFieldDeclaration[]) this.fields.toArray(rv);
        return rv;
    }

    public String toString() {
        String rv = "";
        for (Iterator i = this.fields.iterator(); i.hasNext(); ) {
            rv += "    " + i.next() + "\n";
        }

        return rv;
    }
}

class ASTFieldDeclaration extends ASTNode {
    private ASTPattern pattern;
    private ASTBoolean value;

    public ASTFieldDeclaration(ASTPattern pattern, ASTBoolean value) {
        this.pattern = pattern;
        this.value = value;
    }

    public ASTPattern getPattern() {
        return this.pattern;
    }

    public ASTBoolean getValue() {
        return this.value;
    }

    public String toString() {
        return this.pattern + ": " + this.value + ";";
    }
}

class ASTPattern extends ASTNode {
    private String pattern;
    
    public ASTPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return this.pattern;
    }

    public String toString() {
        return this.pattern;
    }
}

class ASTBoolean extends ASTNode {
    private boolean value;
    
    public ASTBoolean(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}

