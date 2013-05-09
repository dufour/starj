package starj.events;

import java.io.IOException;

import starj.io.traces.TraceInput;
import starj.spec.Constants;

/**
 * An Event corresponding to the <code>JVMPI_OBJECT_ALLOC</code> event. This
 * event is triggered when an object is allocated by the Java VM.
 *
 * @author Bruno Dufour
 * @see ObjectFreeEvent
 * @see ObjectMoveEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class ObjectAllocEvent extends Event implements ArenaEvent, ClassEvent,
        ObjectEvent {
    /* Possible values for is_array */
    /** Indicates that the object is not an array */
    public static final int NORMAL_OBJECT =  0;
    /** Indicates that the object is an array of classes */
    public static final int OBJECT_ARRAY  =  2;
    /** Indicates that the object is an array of <code>boolean</code>s */
    public static final int BOOLEAN_ARRAY =  4;
    /** Indicates that the object is an array of <code>char</code>s */
    public static final int CHAR_ARRAY    =  5;
    /** Indicates that the object is an array of <code>float</code>s */
    public static final int FLOAT_ARRAY   =  6;
    /** Indicates that the object is an array of <code>double</code>s */
    public static final int DOUBLE_ARRAY  =  7;
    /** Indicates that the object is an array of <code>byte</code>s */
    public static final int BYTE_ARRAY    =  8;
    /** Indicates that the object is an array of <code>short</code>s */
    public static final int SHORT_ARRAY   =  9;
    /** Indicates that the object is an array of <code>int</code>s */
    public static final int INT_ARRAY     = 10;
    /** Indicates that the object is an array of <code>long</code>s */
    public static final int LONG_ARRAY    = 11;
    
    /** The ID of the arena where the object is allocated. */
    private int arena_id;
    /**
     * The ID of the class to which the allocated object belongs, or the array
     * element class is <code>is_array</code> is equal to
     * <code>OBJECT_ARRAY</code>.
     */
    private int class_id;
    /** The type of object being allocated.  
     *
     * Possible values are: <code>NORMAL_OBJECT</code>,
     * <code>OBJECT_ARRAY</code>, <code>BOOLEAN_ARRAY</code>,
     * <code>CHAR_ARRAY</code>, <code>FLOAT_ARRAY</code>,
     * <code>DOUBLE_ARRAY</code>, <code>BYTE_ARRAY</code>,
     * <code>SHORT_ARRAY</code>, <code>INT_ARRAY</code>,
     * <code>LONG_ARRAY</code>.
     */
    private int is_array = -1;
    /** The size (in bytes) of the allocated object. */
    private int size;
    /** The ID of the newly allocated object. */
    private int obj_id;

    public ObjectAllocEvent() {
        super(Event.OBJECT_ALLOC);
        this.reset();
    }
    
    public ObjectAllocEvent(int env_id, int arena_id, int class_id,
            int is_array, int size, int obj_id) {
        this(env_id, arena_id, class_id, is_array, size, obj_id, false);
    }

    public ObjectAllocEvent(int env_id, int arena_id, int class_id,
            int is_array, int size, int obj_id, boolean requested) {
        super(Event.OBJECT_ALLOC, env_id, requested);
        this.arena_id = arena_id;
        this.class_id = class_id;
        this.is_array = is_array;
        this.size = size;
        this.obj_id = obj_id;
    }
    
    public int getArenaID() {
        return this.arena_id;
    }
    
    public void setArenaID(int arena_id) {
        this.arena_id = arena_id;
    }

    public int getClassID() {
        return this.class_id;
    }
    
    public void setClassID(int class_id) {
        this.class_id = class_id;
    }

    // NOTE:
    // -----
    // The next two accessors refer to the 'is_array' field,
    // but have been renamed for convenience. Using
    // getIsArray() does not make much sense, and using isArray()
    // makes it sound like a boolean field.

    public int getArrayType() {
        return this.is_array;
    }
    
    public void setArrayType(int array_type) {
        this.is_array = array_type;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }

    public int getObjectID() {
        return this.obj_id;
    }
    
    public void setObjectID(int obj_id) {
        this.obj_id = obj_id;
    }

    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_ARENA_ID) != 0) {
            this.arena_id = in.readArenaID();
        }
        
        if ((mask & Constants.FIELD_OBJECT_ALLOC_CLASS_ID) != 0) {
            this.class_id = in.readClassID();
        }
        
        if ((mask & Constants.FIELD_IS_ARRAY) != 0) {
            this.is_array = in.readInt();
        }
        
        if ((mask & Constants.FIELD_SIZE) != 0) {
            this.size = in.readInt();
        }
        
        if ((mask & Constants.FIELD_OBJ_ID) != 0) {
            this.obj_id = in.readObjectID();
        }
    }

    public void reset() {
        super.reset();

        this.arena_id = 0;
        this.class_id = 0;
        this.is_array = -1;
        this.size = -1;
        
        this.obj_id = 0;
    }
}
