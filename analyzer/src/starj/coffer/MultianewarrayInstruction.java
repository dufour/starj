package starj.coffer;

public class MultianewarrayInstruction extends ConstantPoolInstruction {
    public static final String OPCODE_NAME = "multianewarray";

    private short dimensions;
    
    public MultianewarrayInstruction(int offset, ConstantPool cp, int index,
            short dimensions) {
        super(Code.MULTIANEWARRAY, 4, offset, cp, index);
        this.dimensions = dimensions;
    }

    public short getDimensions() {
        return this.dimensions;
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}
