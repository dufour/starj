package starj.util.text;

public class StringInputBuffer implements StringInput {
    private int offset;
    private String data;
    
    public StringInputBuffer() {
        this.offset = 0;
        this.data = "";
    }

    public StringInputBuffer(CharSequence s) {
        this.offset = 0;
        this.data = s.toString();
    }

    public int available() {
        int rv = this.data.length() - this.offset;
        if (rv < 0) {
            return 0;
        }

        return rv;
    }

    public boolean isEmpty() {
        return this.available() < 1;
    }

    public void reset() {
        this.offset = 0;
    }

    public void reset(CharSequence sequence) {
        this.reset();
        this.data = sequence.toString();
    }
    
    public char readChar() {
        return this.data.charAt(this.offset++);
    }
    
    public String read() {
        String rv = this.data.substring(this.offset);
        this.offset = this.data.length();
        return rv;
    }

    public String read(int length) {
        int start = this.offset;
        if (length > this.available()) {
            length = this.available();
        }
        int end = start + length;
        String rv = this.data.substring(start, end);
        this.offset = end;
        return rv;
    }
    
    public char peekChar() {
        return this.data.charAt(this.offset);
    }

    public String peek() {
        return this.data.substring(this.offset);
    }
    
    public String peek(int length) {
        int start = this.offset;
        int end = start + this.data.length();
        
        return this.data.substring(start, end);
    }
    
    public int skip(int count) {
        int max_count = this.available();
        if (count > max_count) {
            count = max_count;
        }
        this.offset += count;
        return count;
    }

    public int indexOf(int ch) {
        int p = this.data.indexOf(ch, this.offset);
        if (p >= 0) {
            p -= this.offset;
        }
        return p;
    }

    public int indexOf(int ch, int fromIndex) {
        int p = this.data.indexOf(ch, this.offset + fromIndex);
        if (p >= 0) {
            p -= this.offset;
        }
        return p;
    }
    
    public int indexOf(String str) {
        int p = this.data.indexOf(str, this.offset);
        if (p >= 0) {
            p -= this.offset;
        }
        return p;
    }

    public int indexOf(String str, int fromIndex) {
        int p = this.data.indexOf(str, this.offset + fromIndex);
        if (p >= 0) {
            p -= this.offset;
        }
        return p;
    }

    public String toString() {
        return this.peek();
    }
}
