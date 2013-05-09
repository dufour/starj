package starj.dependencies;

public class InstructionEventDependency extends EventDependency {
    private short[] opcodes;

    public InstructionEventDependency(int event_id, FieldMask mask) {
        super(event_id, mask);
    }

    public InstructionEventDependency(int event_id, FieldMask mask,
            EventDependency alternate) {
        super(event_id, mask, alternate);
    }
    
    public InstructionEventDependency(int event_id, FieldMask mask,
            boolean required) {
        super(event_id, mask, required);
    }

    public InstructionEventDependency(int event_id, FieldMask mask,
            boolean required, EventDependency alternate) {
        super(event_id, mask, required, alternate);
    }

    public InstructionEventDependency(int event_id, FieldMask mask,
            short[] opcodes) {
        super(event_id, mask);
        this.opcodes = opcodes;
    }

    public InstructionEventDependency(int event_id, FieldMask mask,
            EventDependency alternate, short[] opcodes) {
        super(event_id, mask, alternate);
        this.opcodes = opcodes;
    }
    
    public InstructionEventDependency(int event_id, FieldMask mask,
            boolean required, short[] opcodes) {
        super(event_id, mask, required);
        this.opcodes = opcodes;
    }

    public InstructionEventDependency(int event_id, FieldMask mask,
            boolean required, EventDependency alternate, short[] opcodes) {
        super(event_id, mask, required, alternate);
        this.opcodes = opcodes;
    }

    public short[] getOpcodes() {
        if (this.opcodes == null) {
            this.opcodes = new short[0];
        }
        return this.opcodes;
    }
}
