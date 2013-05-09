package starj.coffer.types;

import java.util.*;

import starj.util.text.StringInput;
import starj.util.text.StringInputBuffer;

public class TypeRepository {
    private static TypeRepository instance;

    public static final char BOOL_TYPE_DESC   = 'Z';
    public static final char BYTE_TYPE_DESC   = 'B';
    public static final char CHAR_TYPE_DESC   = 'C';
    public static final char DOUBLE_TYPE_DESC = 'D';
    public static final char FLOAT_TYPE_DESC  = 'F';
    public static final char INT_TYPE_DESC    = 'I';
    public static final char LONG_TYPE_DESC   = 'J';
    public static final char SHORT_TYPE_DESC  = 'S';
    public static final char VOID_TYPE_DESC   = 'V';
    public static final char CLASS_TYPE_DESC  = 'L';
    public static final char ARRAY_TYPE_DESC  = '[';

    public static final Type BOOL_TYPE   = new BoolType();
    public static final Type BYTE_TYPE   = new ByteType();
    public static final Type CHAR_TYPE   = new CharType();
    public static final Type DOUBLE_TYPE = new DoubleType();
    public static final Type FLOAT_TYPE  = new FloatType();
    public static final Type INT_TYPE    = new IntType();
    public static final Type LONG_TYPE   = new LongType();
    public static final Type SHORT_TYPE  = new ShortType();
    public static final Type VOID_TYPE   = new VoidType();

    public static final Type BOOL_ARRAY_TYPE   = new ArrayType(BOOL_TYPE);
    public static final Type BYTE_ARRAY_TYPE   = new ArrayType(BYTE_TYPE);
    public static final Type CHAR_ARRAY_TYPE   = new ArrayType(CHAR_TYPE);
    public static final Type DOUBLE_ARRAY_TYPE = new ArrayType(DOUBLE_TYPE);
    public static final Type FLOAT_ARRAY_TYPE  = new ArrayType(FLOAT_TYPE);
    public static final Type INT_ARRAY_TYPE    = new ArrayType(INT_TYPE);
    public static final Type LONG_ARRAY_TYPE   = new ArrayType(LONG_TYPE);
    public static final Type SHORT_ARRAY_TYPE  = new ArrayType(SHORT_TYPE);

    private Map known_types;
    
    private TypeRepository() {
        // no instances
        this.known_types = new HashMap();

        this.known_types.put(makeArrayType(BYTE_TYPE_DESC),
                BYTE_ARRAY_TYPE);
        this.known_types.put(makeArrayType(BOOL_TYPE_DESC),
                BOOL_ARRAY_TYPE);
        this.known_types.put(makeArrayType(CHAR_TYPE_DESC),
                CHAR_ARRAY_TYPE);
        this.known_types.put(makeArrayType(DOUBLE_TYPE_DESC),
                DOUBLE_ARRAY_TYPE);
        this.known_types.put(makeArrayType(FLOAT_TYPE_DESC),
                FLOAT_ARRAY_TYPE);
        this.known_types.put(makeArrayType(INT_TYPE_DESC),
                INT_ARRAY_TYPE);
        this.known_types.put(makeArrayType(LONG_TYPE_DESC),
                LONG_ARRAY_TYPE);
        this.known_types.put(makeArrayType(SHORT_TYPE_DESC),
                SHORT_ARRAY_TYPE);
    }

    private static String makeArrayType(char type_desc) {
        return String.valueOf(ARRAY_TYPE_DESC) + type_desc;
    }

    public static TypeRepository v() {
        if (instance == null) {
            instance = new TypeRepository();
        }

        return instance;
    }

    public Type getType(String type) {
        /* Check our growing database */
        if ((type == null) || (type.length() == 0)) {
            return null;
        }

        Type t = (Type) this.known_types.get(type);
        if (t != null) {
            return t;
        }

        StringInput type_input = new StringInputBuffer(type);
        t = this.parseType(type_input);

        if (t == null || !type_input.isEmpty()) {
            throw new RuntimeException("Invalid type encoding: " + type);
        }
        return t;
    }

    public Type getClassType(String class_name) {
        if (class_name != null && class_name.length() > 0
                && class_name.charAt(0) == ARRAY_TYPE_DESC) {
            // Array type
            return this.getType(class_name);
        } else {
            // Class type
            return this.getType(CLASS_TYPE_DESC + class_name + ";");
        }
    }

    private Type parseType(StringInput type) {
        if (type.available() < 1) {
            return null;
        }

        char c = type.readChar();
        switch (c) {
            case BOOL_TYPE_DESC:
                return BOOL_TYPE;
            case BYTE_TYPE_DESC:
                return BYTE_TYPE;
            case CHAR_TYPE_DESC:
                return CHAR_TYPE;
            case DOUBLE_TYPE_DESC:
                return DOUBLE_TYPE;
            case FLOAT_TYPE_DESC:
                return FLOAT_TYPE;
            case INT_TYPE_DESC:
                return INT_TYPE;
            case LONG_TYPE_DESC:
                return LONG_TYPE;
            case SHORT_TYPE_DESC:
                return SHORT_TYPE;
            case VOID_TYPE_DESC:
                return VOID_TYPE;
            case CLASS_TYPE_DESC: {
                    int semicol_pos = type.indexOf(';');
                    // read class name
                    String class_name = type.read(semicol_pos);
                    // read ';'
                    type.readChar();
                    String type_encoding = CLASS_TYPE_DESC + class_name + ";";
                    if (this.known_types.containsKey(type_encoding)) {
                        return (Type) this.known_types.get(type_encoding);
                    }
                    Type t = new ObjectType(class_name);
                    this.known_types.put(type_encoding, t);
                    return t;
                }
            case ARRAY_TYPE_DESC: {
                    // Recursively parse the array type
                    Type t = new ArrayType(this.parseType(type));
                    String type_encoding = t.toTypeString();
                    if (this.known_types.containsKey(type_encoding)) {
                        return (Type) this.known_types.get(type_encoding);
                    }

                    this.known_types.put(type_encoding, t);
                    return t;
                }
            default:
                break;
        }

        throw new RuntimeException("Invalid type: " + c + type);
    }

    /**
     * Parses a sequence of types, as found in method signatures.
     * 
     * @param types the textual representation of a sequence of types,
     * i.e. a string consisting of one or more consecutive textual
     * type descriptors, or the empty string.
     * @return an array of {@link Type Type} objects corresponding
     *     to the textual representation passed as argument.
     */
    public Type[] parseTypes(String types) {
        List parsed_types = new LinkedList();
        StringInput type_input = new StringInputBuffer(types);
        while (!type_input.isEmpty()) {
            parsed_types.add(this.parseType(type_input));
        }

        Type[] rv = new Type[parsed_types.size()];
        parsed_types.toArray(rv);
        return rv;
    }
}
