package starj.coffer;

public class NewarrayInstruction extends Instruction {
    public static final String OPCODE_NAME = "newarray";

    public static final byte T_BOOLEAN = ((byte)  4);
    public static final byte T_CHAR    = ((byte)  5);
    public static final byte T_FLOAT   = ((byte)  6);
    public static final byte T_DOUBLE  = ((byte)  7);
    public static final byte T_BYTE    = ((byte)  8);
    public static final byte T_SHORT   = ((byte)  9);
    public static final byte T_INT     = ((byte) 10);
    public static final byte T_LONG    = ((byte) 11);
    
    private byte type;
    
    public NewarrayInstruction(int offset, byte type) {
        super(Code.NEWARRAY, 2, offset);
        this.type = type;
    }

    public byte getType() {
        return this.type;
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

