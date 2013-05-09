package starj.coffer;

public abstract class BooleanInstruction extends Instruction {
    public BooleanInstruction(short opcode, int length, int offset) {
        super(opcode, length, offset);
    }
}

