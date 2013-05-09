package starj.coffer;

public abstract class BitwiseInstruction extends Instruction {
    public BitwiseInstruction(short opcode, int length, int offset) {
        super(opcode, length, offset);
    }
}

