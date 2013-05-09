package starj.events;

import java.io.IOException;

import starj.spec.Constants;
import starj.io.traces.TraceInput;

/**
 * An Event corresponding to the <code>JVMPI_CLASS_LOAD</code> event. This
 * event is triggered when a class is loaded by the Java VM.
 *
 * @author Bruno Dufour
 * @see ClassUnloadEvent
 * @see <a href="http://java.sun.com/j2se/1.4/docs/guide/jvmpi/jvmpi.html">The
 *      Java Virtual Machine Profiler Interface (JVMPI)</a>
 */
public class ClassLoadEvent extends Event implements ClassEvent {
    /** The name of the class being loaded. */
    private String class_name;
    /** The name of the source file that defines the class being loaded. */
    private String source_name;
    /** The number of interfaces implemented by the class being loaded. */
    private int num_interfaces;
    /** The number of methods defined in the class being loaded. */
    private int num_methods;
    /** An array of the methods defined in the class being loaded. */
    private JVMPIMethod[] methods;
    /** The number of static fields defined in the class being loaded. */
    private int num_static_fields;
    /** An array of the static fields defined in the class being loaded. */
    private JVMPIField[] statics;
    /** The number of instance fields defined in the class being loaded. */
    private int num_instance_fields;
    /** An array of the instance fields defined in the class being loaded. */
    private JVMPIField[] instances;
    /** The ID of the class being loaded. */
    private int class_id;

    public ClassLoadEvent() {
        super(Event.CLASS_LOAD);
        this.reset();
    }
    
    public ClassLoadEvent(int env_id, String class_name, String source_name,
            int num_interfaces, JVMPIMethod[] methods, JVMPIField[] statics,
            JVMPIField[] instances, int class_id) {
        this(env_id, class_name, source_name, num_interfaces, methods, statics,
                instances, class_id, false);
    }
    
    public ClassLoadEvent(int env_id, String class_name, String source_name,
            int num_interfaces, JVMPIMethod[] methods, JVMPIField[] statics,
            JVMPIField[] instances, int class_id, boolean requested) {
        super(Event.CLASS_LOAD, env_id, requested);
        this.class_name = class_name;
        this.source_name = source_name;
        this.num_interfaces = num_interfaces;
        this.num_methods = (methods != null ? methods.length : 0);
        this.methods = methods;
        this.num_static_fields = (statics != null ? statics.length : 0);
        this.statics = statics;
        this.num_instance_fields = (instances != null ? instances.length : 0);
        this.instances = instances;
        this.class_id = class_id;
    }

    public String getClassName() {
        return this.class_name;
    }
    
    public void setClassName(String class_name) {
        this.class_name = class_name;
    }
    
    public String getSourceName() {
        return this.source_name;
    }
    
    public void setSourceName(String source_name) {
        this.source_name = source_name;
    }
    
    public int getInterfaceCount() {
        return this.num_interfaces;
    }
    
    public void setInterfaceCount(int num_interfaces) {
        this.num_interfaces = num_interfaces;
    }
    
    public int getMethodCount() {
        return this.num_methods;
    }
    
    public JVMPIMethod[] getMethods() {
        return this.methods;
    }
    
    public JVMPIMethod getMethod(int index) {
        return this.methods[index];
    }
    
    public void setMethods(JVMPIMethod[] methods) {
        this.methods = methods;
        if (methods == null) {
            this.num_methods = 0;
        } else {
            this.num_methods = methods.length;
        }
    }
    
    public void setMethod(JVMPIMethod method, int index) {
        this.methods[index] = method;
    }
    
    public int getNumStaticFields() {
        return this.num_static_fields;
    }
    
    public JVMPIField[] getStatics() {
        return this.statics;
    }
    
    public JVMPIField getStatics(int index) {
        return this.statics[index];
    }
    
    public void setStatics(JVMPIField[] statics) {
        this.statics = statics;
        if (statics == null) {
            this.num_static_fields = 0;
        } else {
            this.num_static_fields = statics.length;
        }
    }
    
    public void setStatics(JVMPIField statik, int index) {
        this.statics[index] = statik;
    }
    
    public int getNumInstanceFields() {
        return this.num_instance_fields;
    }
    
    public JVMPIField[] getInstances() {
        return this.instances;
    }
    
    public JVMPIField getInstances(int index) {
        return this.instances[index];
    }
    
    public void setInstances(JVMPIField[] instances) {
        this.instances = instances;
        if (instances == null) {
            this.num_instance_fields = 0;
        } else {
            this.num_instance_fields = instances.length;
        }
    }
    
    public void setInstances(JVMPIField instance, int index) {
        this.instances[index] = instance;
    }
    
    public int getClassID() {
        return this.class_id;
    }
    
    public void setClassID(int class_id) {
        this.class_id = class_id;
    }
    
    public void readFromStream(TraceInput in, int mask, boolean requested)
            throws IOException {
        super.readFromStream(in, mask, requested);

        if ((mask & Constants.FIELD_CLASS_NAME) != 0) {
            this.class_name = in.readUTF();
        }
        
        if ((mask & Constants.FIELD_SOURCE_NAME) != 0) {
            this.source_name = in.readUTF();
        }
        
        if ((mask & Constants.FIELD_NUM_INTERFACES) != 0) {
            this.num_interfaces = in.readInt();
        }
        
        if ((mask & Constants.FIELD_NUM_METHODS) != 0
                || (mask & Constants.FIELD_METHODS) != 0) {
            this.num_methods = in.readInt();
        }
        
        if ((mask & Constants.FIELD_METHODS) != 0 
                && this.num_methods > 0) {
            this.methods = new JVMPIMethod[this.num_methods];
            for (int i = 0; i < this.num_methods; i++) {
                this.methods[i] = new JVMPIMethod(in);
            }
        }
        
        if ((mask & Constants.FIELD_NUM_STATIC_FIELDS) != 0
                || (mask & Constants.FIELD_STATICS) != 0) {
            this.num_static_fields = in.readInt();
        }
        
        if ((mask & Constants.FIELD_STATICS) != 0 
                && this.num_static_fields > 0) {
            this.statics = new JVMPIField[this.num_static_fields];
            for (int i = 0; i < this.num_static_fields; i++) {
                this.statics[i] = new JVMPIField(in);
            }
        }

        if ((mask & Constants.FIELD_NUM_INSTANCE_FIELDS) != 0
                || (mask & Constants.FIELD_INSTANCES) != 0) {
            this.num_instance_fields = in.readInt();
        }
        
        if ((mask & Constants.FIELD_INSTANCES) != 0 
                && this.num_instance_fields > 0) {
            this.instances = new JVMPIField[this.num_instance_fields];
            for (int i = 0; i < this.num_instance_fields; i++) {
                this.instances[i] = new JVMPIField(in);
            }
        } 

        if ((mask & Constants.FIELD_CLASS_LOAD_CLASS_ID) != 0) {
            this.class_id = in.readClassID();
        }
    }

    public void reset() {
        super.reset();
        
        this.class_name = null;
        this.source_name = null;
        
        this.num_interfaces = 0;
        
        this.num_methods = 0;
        this.methods = null;
        
        this.num_static_fields = 0;
        this.statics = null;
        
        this.num_instance_fields = 0;
        this.instances = null;
        
        this.class_id = 0;
    }
}
