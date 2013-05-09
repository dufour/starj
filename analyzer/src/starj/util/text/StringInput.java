package starj.util.text;

public interface StringInput {
    public int available();
    public char readChar();
    public String read();
    public String read(int length);
    public char peekChar();
    public String peek();
    public String peek(int length);
    public int skip(int count);
    public boolean isEmpty();
    public int indexOf(int ch);
    public int indexOf(int ch, int fromIndex);
    public int indexOf(String str);
    public int indexOf(String str, int fromIndex);
}
