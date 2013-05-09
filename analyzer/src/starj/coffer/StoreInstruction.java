package starj.coffer;

public abstract class StoreInstruction extends VariableInstruction {
    public StoreInstruction(short opcode, int length, int offset, short index) {
        super(opcode, length, offset, index);
    }
}
