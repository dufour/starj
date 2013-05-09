package starj.coffer;

public abstract class ReturningInstruction extends Instruction {
    public ReturningInstruction(short opcode, int length, int offset) {
        super(opcode, length, offset);
    }
}
