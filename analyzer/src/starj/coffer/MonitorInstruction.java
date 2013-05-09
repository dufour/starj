package starj.coffer;

public abstract class MonitorInstruction extends Instruction {
    public MonitorInstruction(short opcode, int length, int offset) {
        super(opcode, length, offset);
    }
}

