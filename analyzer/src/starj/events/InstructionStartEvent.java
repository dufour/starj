package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;
import starj.spec.Constants;
import starj.coffer.Instruction;

/**
 * An Event corresponding to the <code>JVMPI_INSTRUCTION_START</code> event.
 * This event is triggered when an instruction is executed by the Java VM.
 *
 * @author Bruno Dufour
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class InstructionStartEvent extends Event implements MethodEvent {
    private int method_id;
    
    /** The offset of this instruction in the code of the method. */
    private int offset;
    /**
     * Indicates, in case of an <code>if</code> bytecode, whether the true
     * branch is taken.
     */
    private boolean is_true;
    private int key;
    private int low;
    private int hi;
    private int chosen_pair_index;
    private int pairs_total;
    /** The bytecode corresponding to this instruction. This information
     * is not available from JVMPI, and thus has to be set by some
     * analysis. */
    private short opcode;
    /** The {@link starj.coffer.Instruction Instrucion} object corresponding to
     * this instruction. This information
     * is not available from JVMPI, and thus has to be set by some
     * analysis. */
    private Instruction instruction;
    
    public InstructionStartEvent() {
        super(Event.INSTRUCTION_START);
    }
    
    public InstructionStartEvent(int env_id, int method_id, int offset,
            boolean is_true, int key, int low, int hi, int chosen_pair_index,
            int pairs_total) {
        this(env_id, method_id, offset, is_true, key, low, hi,
                chosen_pair_index, pairs_total, false);
    }

    public InstructionStartEvent(int env_id, int method_id, int offset,
            boolean is_true, int key, int low, int hi, int chosen_pair_index,
            int pairs_total, boolean requested) {
        super(Event.INSTRUCTION_START, env_id, requested);
        this.method_id = method_id;
        this.offset = offset;
        this.is_true = is_true;
        this.key = key;
        this.low = low;
        this.hi = hi;
        this.chosen_pair_index = chosen_pair_index;
        this.pairs_total = pairs_total;
    }
    
    public int getMethodID() {
        return this.method_id;
    }
    
    public void setMethodID(int method_id) {
        this.method_id = method_id;
    }

    public int getOffset() {
        return this.offset;
    }
    
    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    public boolean getIsTrue() {
        return this.is_true;
    }
    
    public void setIsTrue(boolean is_true) {
        this.is_true = is_true;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setKey(int key) {
        this.key = key;
    }
    
    public int getLow() {
        return this.low;
    }
    
    public void setLow(int low) {
        this.low = low;
    }
    
    public int getHi() {
        return this.hi;
    }
    
    public void setHi(int hi) {
        this.hi = hi;
    }
    
    public int getChosenPairIndex() {
        return this.chosen_pair_index;
    }
    
    public void setChosenPairIndex(int chosen_pair_index) {
        this.chosen_pair_index = chosen_pair_index;
    }
    
    public int getPairsTotal() {
        return this.pairs_total;
    }
    
    public void setPairsTotal(int pairs_total) {
        this.pairs_total = pairs_total;
    }
    
    public short getOpcode() {
        return this.opcode;
    }
    
    public void setOpcode(short opcode) {
        this.opcode = opcode;
    }

    public Instruction getInstruction() {
        return this.instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_METHOD_ID) != 0) {
            this.method_id = in.readMethodID();
        } 
        
        if ((mask & Constants.FIELD_OFFSET) != 0) {
            this.offset = in.readInstructionOffset();
        } 
        
        if ((mask & Constants.FIELD_IS_TRUE) != 0) {
            this.is_true = in.readBoolean();
        }
        
        if ((mask & Constants.FIELD_KEY) != 0) {
            this.key = in.readInt();
        } 
        
        if ((mask & Constants.FIELD_LOW) != 0) {
            this.low = in.readInt();
        }
        
        if ((mask & Constants.FIELD_HI) != 0) {
            this.hi = in.readInt();
        }
        
        if ((mask & Constants.FIELD_CHOSEN_PAIR_INDEX) != 0) {
            this.chosen_pair_index = in.readInt();
        }
        
        if ((mask & Constants.FIELD_PAIRS_TOTAL) != 0) {
            this.pairs_total = in.readInt();
        }
    }

    public void reset() {
        super.reset();
        
        this.method_id = 0;

        this.offset = -1;
        this.is_true = false;
        this.key = -1;
        this.low = -1;
        this.hi = -1;
        this.chosen_pair_index = -1;
        this.pairs_total = -1;
        
        this.opcode = -1;
        this.instruction = null;
    }
}
