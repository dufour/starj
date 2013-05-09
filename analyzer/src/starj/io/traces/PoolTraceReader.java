package starj.io.traces;

import java.io.*;
import java.util.*;

import starj.coffer.*;
import starj.events.*;
import starj.io.logging.LogManager;
import starj.io.logging.Logger;
import starj.spec.Constants;
import starj.toolkits.services.IDResolver;
import starj.toolkits.services.MethodEntity;
import starj.util.StringUtils;

public class PoolTraceReader implements TraceReader {
    public static final int TRACE_MAGIC        = 0x5AB1E5EF;
    public static final int REQUESTED_MASK     = 0x00000080;
    public static final int FILE_SPLIT         = 0x000000FF;
    public static final int COMPACT_INST_START = 0x000000FE;

    // Trace data
    private short minor_version;
    private short major_version;
    private int[] event_masks;
    private Map attributes;

    // Reader state
    private boolean closed;
    private TraceInput input;
    private long[] runtime_counts;
    private long[] trace_counts;
    private long event_count;
    private Event[] event_pool;
    private InstructionPredictor predictor;
    // Pending instruction state
    private int pending_inst_count;
    private Instruction pending_inst;
    private int pending_inst_env_id;
    private int pending_inst_mid;

    public PoolTraceReader(InputStream stream) throws IOException,
            TraceIOException {
        this((TraceInput) new TraceInputStream(stream));
    }

    public PoolTraceReader(TraceInput input) throws IOException,
            TraceIOException {
        this.input = input;
        int magic = input.readInt();
        if (magic != TRACE_MAGIC) {
            throw new TraceFormatException("Invalid magic number: 0x"
                    + StringUtils.justifyRight(Integer.toHexString(magic),
                    8, '0'));
        }

        this.minor_version = input.readShort();
        this.major_version = input.readShort();
        if (!this.supports(this.major_version, this.minor_version)) {
            throw new TraceFormatException("Unsupported version: "
                    + this.major_version + "." + this.minor_version);
        }
        
        this.predictor = DefaultPredictor.v(); // Define a default value
        this.readHeader();
        this.closed = false;
        this.runtime_counts = new long[Event.EVENT_COUNT];
        this.event_count = 0L;
        this.initializePool();
    }

    private void readHeader() throws TraceIOException, IOException {
        this.trace_counts = new long[Event.EVENT_COUNT];

        // read masks
        this.event_masks = new int[Event.EVENT_COUNT];
        int num_masks = this.input.readInt();
        for (int i = 0; i < num_masks; i++) {
            int id = this.input.readEventID();
            int mask = this.input.readFieldMask();
            if ((mask & Constants.FIELD_COUNTED) != 0) {
                this.trace_counts[id] = this.input.readLong();
            } else {
                this.trace_counts[id] = -1L;
            }
            this.event_masks[id] = mask;
        }

        // read attributes
        int attrib_count = this.input.readInt();
        this.attributes = new HashMap();
        for (int i = 0; i < attrib_count; i++) {
            String attrib_name = this.input.readUTF();
            int length = this.input.readInt();
            TraceAttribute attrib;
            if (attrib_name.equals("starj.pointer.size")) {
                attrib = new PointerSizeAttribute(attrib_name, length,
                        this.input);
            } else if (attrib_name.equals("starj.bytecode.predictor")) {
                InstructionPredictorAttribute ipa
                        = new InstructionPredictorAttribute(attrib_name,
                                length, this.input);
                this.predictor = ipa.getInstructionPredictor();
                attrib = ipa;
            } else {
                LogManager.v().logWarning("Unknown trace attribute: '"
                        + attrib_name + "'");
                attrib = new GenericTraceAttribute(attrib_name, length,
                        this.input);
            }
            this.attributes.put(attrib_name, attrib);
        }
    }

    private void initializePool() {
        Event[] p = new Event[Event.EVENT_COUNT];

        p[Event.ARENA_DELETE]              = new ArenaDeleteEvent();
        p[Event.ARENA_NEW]                 = new ArenaNewEvent();
        
        p[Event.CLASS_LOAD]                = new ClassLoadEvent();
        p[Event.CLASS_LOAD_HOOK]           = new ClassLoadHookEvent();
        p[Event.CLASS_UNLOAD]              = new ClassUnloadEvent();
        
        p[Event.COMPILED_METHOD_LOAD]      = new CompiledMethodLoadEvent();
        p[Event.COMPILED_METHOD_UNLOAD]    = new CompiledMethodUnloadEvent();
        
        p[Event.GC_FINISH]                 = new GCFinishEvent();
        p[Event.GC_START]                  = new GCStartEvent();
        p[Event.JVM_INIT_DONE]             = new JVMInitDoneEvent();        
        p[Event.JVM_SHUT_DOWN]             = new JVMShutDownEvent();
        
        p[Event.METHOD_ENTRY]              = new MethodEntryEvent();
        p[Event.METHOD_ENTRY2]             = new MethodEntry2Event();
        p[Event.METHOD_EXIT]               = new MethodExitEvent();

        p[Event.MONITOR_CONTENDED_ENTER]   = new MonitorContendedEnterEvent();
        p[Event.MONITOR_CONTENDED_ENTERED] = new MonitorContendedEnteredEvent();
        p[Event.MONITOR_CONTENDED_EXIT]    = new MonitorContendedExitEvent();
        p[Event.MONITOR_WAIT]              = new MonitorWaitEvent();
        p[Event.MONITOR_WAITED]            = new MonitorWaitedEvent();
        
        p[Event.OBJECT_ALLOC]              = new ObjectAllocEvent();
        p[Event.OBJECT_FREE]               = new ObjectFreeEvent();
        p[Event.OBJECT_MOVE]               = new ObjectMoveEvent();

        p[Event.THREAD_END]                = new ThreadEndEvent();
        p[Event.THREAD_START]              = new ThreadStartEvent();

        p[Event.INSTRUCTION_START]         = new InstructionStartEvent();

        this.event_pool = p;
    }

    public short getMinorVersion() {
        return this.minor_version;
    }
    
    public short getMajorVersion() {
        return this.major_version;
    }

    public boolean supports(short major_version, short minor_version) {
        return (major_version == 1 && minor_version == 0);
    }

    public int getFieldMask(int event_id) {
        return this.event_masks[event_id];
    }

    public int getAttributeCount() {
        return this.attributes.size();
    }

    public boolean hasAttribute(String name) {
        return this.attributes.containsKey(name);
    }

    public TraceAttribute getAttribute(String name) {
        return (TraceAttribute) this.attributes.get(name);
    }

    public TraceAttribute[] getAttributes() {
        TraceAttribute[] rv = new TraceAttribute[this.attributes.size()];
        this.attributes.values().toArray(rv);
        return rv;
    }
    
    public Event getNextEvent() throws TraceIOException, IOException {
        if (this.closed) {
            return null;
        }

        if (this.pending_inst_count > 0) {
            return this.getPendingInstructionEvent();
        }

        TraceInput input = this.input;

        int eventID;
        try {
            eventID = input.readEventID();
        } catch (EOFException e) {
            this.closed = true;
            return null;
        }

        while (eventID == FILE_SPLIT) {
            String next_filename = input.readUTF();
            LogManager.v().logMessage("Trace file continued on file: '"
                    + next_filename + "'", Logger.INFORMATIVE);
            InputStream new_stream
                    = InputStreamFactory.v().getInputStream(next_filename);
            input = new TraceInputStream(new_stream);
            this.input = input;
            
            /* Get the next type ID from the new file */
            eventID = input.readEventID();
        }

        // Check for special events. For efficiency, the checks
        // are performed in decreasing order of probability
        if (eventID == COMPACT_INST_START) {
            this.pending_inst_env_id = input.readThreadID();
            this.pending_inst_mid = input.readMethodID();
            int initial_offset = input.readInstructionOffset();
            this.pending_inst_count = input.readInstructionOffset();
            MethodEntity method_entity
                    = IDResolver.v().getMethodEntity(this.pending_inst_mid);
            if (method_entity != null) {
                Method m = method_entity.getMethod();
                if (m != null) {
                    Code c = m.getCode();
                    if (c != null) {
                        Instruction inst = c.lookup(initial_offset);
                        if (inst != null) {
                            this.pending_inst = inst;
                            return this.getPendingInstructionEvent();
                        }
                    }
                }
            }
                
            this.closed = true;
            throw new TraceIOException("Failed to obtain code for a "
                    + "compressed bytecode sequence");
        }

        // Clear 'requested' bit
        boolean requested = ((eventID & REQUESTED_MASK) != 0);
        eventID = (eventID & ~REQUESTED_MASK) & 0x000000FF;

        Event event = null;
        try {
            int mask = this.event_masks[eventID];
            event = this.event_pool[eventID];
            event.readFromStream(input, mask, requested);
            this.runtime_counts[eventID] += 1L;
        } catch (ArrayIndexOutOfBoundsException e) {
            this.closed = true;
            throw new TraceIOException("Unsupported event ID: " + eventID);
        } catch (NullPointerException e) {
            // Using an exception handler avoids one null check per event
            if (event == null) {
                this.closed = true;
                throw new TraceIOException("Event not implemented: " + eventID);
            } else {
                throw e;
            }
        }

        this.event_count += 1L;
        return event;
    }

    public long getCurrentEventCount() {
        return this.event_count;
    }

    private InstructionStartEvent getPendingInstructionEvent() {
        InstructionStartEvent ise = (InstructionStartEvent)
                this.event_pool[Event.INSTRUCTION_START];
        ise.reset();

        ise.setEnvID(this.pending_inst_env_id);
        ise.setMethodID(this.pending_inst_mid);
        ise.setOffset(this.pending_inst.getOffset());
        this.pending_inst = this.predictor.predict(this.pending_inst);
        this.pending_inst_count -= 1;

        this.event_count += 1L;
        this.runtime_counts[Event.INSTRUCTION_START] += 1L;
        return ise;
    }
}
