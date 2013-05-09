package starj.coffer;

public abstract class ConstantPoolInstruction extends Instruction {
    private int index;
    private ConstantPool cp;
    
    public ConstantPoolInstruction(short opcode, int length, int offset,
            ConstantPool cp, int index) {
        super(opcode, length, offset);
        this.cp = cp;
        this.index = index;
    }
    
    public ConstantPool getConstantPool() {
        return this.cp;
    }

    public int getIndex() {
        return this.index;
    }

    public Constant getConstant() {
        return this.cp.get(this.index);
    }
}

