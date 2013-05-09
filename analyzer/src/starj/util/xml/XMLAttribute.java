package starj.util.xml;

public class XMLAttribute {
    private String name;
    private String value;

    public XMLAttribute(String name) {
        this.name = name;
        this.value = null;
    }

    public XMLAttribute(String name, boolean value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public XMLAttribute(String name, char value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public XMLAttribute(String name, char[] value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public XMLAttribute(String name, double value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public XMLAttribute(String name, float value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public XMLAttribute(String name, int value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public XMLAttribute(String name, long value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public XMLAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public XMLAttribute(String name, Object value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return this.name + "=\"" + this.value + "\"";
    }
}
