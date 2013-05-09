package starj.toolkits.metrics;

public class StringKey implements BinKey {
    private String value;
    
    public StringKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public int hashCode() {
        return (this.value != null ? this.value.hashCode() : 0);
    }

    public boolean equals(Object obj) {
        if (obj instanceof StringKey) {
            String s = ((StringKey) obj).value;

            return this.value == s || (s != null && s.equals(this.value));
        }

        return false;
    }

    public String toString() {
        return this.value;
    }
    
    public String getType() {
        return "string";
    }
}
