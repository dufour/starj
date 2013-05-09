package starj.coffer;

public abstract class LoadInstruction extends VariableInstruction {
    public LoadInstruction(short opcode, int length, int offset, short index) {
        super(opcode, length, offset, index);
    }
}
