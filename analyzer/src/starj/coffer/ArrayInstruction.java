package starj.coffer;

public abstract class ArrayInstruction extends Instruction {
    public ArrayInstruction(short opcode, int length, int offset) {
        super(opcode, length, offset);
    }
}
