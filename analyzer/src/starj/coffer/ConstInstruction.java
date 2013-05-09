package starj.coffer;

public abstract class ConstInstruction extends Instruction {
    public ConstInstruction(short opcode, int length, int offset) {
        super(opcode, length, offset);
    }

    public abstract Object getValue();
}
