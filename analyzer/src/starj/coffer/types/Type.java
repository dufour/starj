package starj.coffer.types;

public interface Type extends Comparable {
    public static final int ARRAY_TYPE  =  1;
    public static final int BOOL_TYPE   =  2;
    public static final int BYTE_TYPE   =  3;
    public static final int CHAR_TYPE   =  4;
    public static final int DOUBLE_TYPE =  5;
    public static final int FLOAT_TYPE  =  6;
    public static final int INT_TYPE    =  7;
    public static final int LONG_TYPE   =  8;
    public static final int SHORT_TYPE  =  9;
    public static final int OBJECT_TYPE = 10;
    public static final int VOID_TYPE   = 11;

    public String toTypeString();
    public int getTypeID();
}
