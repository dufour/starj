package starj.coffer;

public abstract class BranchInstruction extends Instruction {
    private Instruction target;
    private int target_offset;

    public BranchInstruction(short opcode, int length, int offset, int target) {
        super(opcode, length, offset);
        this.target_offset = target;
    }

    public int getTargetOffset() {
        return this.target_offset;
    }

    void setTarget(Instruction target) {
        this.target = target;
    }

    public Instruction getTarget() {
        return this.target;
    }
}

