package starj.coffer;

public abstract class VariableInstruction extends Instruction {
    private short index;
    
    public VariableInstruction(short opcode, int length, int offset,
            short index) {
        super(opcode, length, offset);
        this.index = index;
    }

    public short getIndex() {
        return this.index;
    }
}

