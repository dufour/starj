package starj.spec;

import java.io.*;

import starj.events.Event;
import starj.io.traces.*;

public class SpecReader {
    private DataInputStream input;
    private int[] masks;
    
    public SpecReader(String filename) throws IOException, TraceIOException {
        this(new FileInputStream(filename));
    }

    public SpecReader(File file) throws IOException, TraceIOException {
        this(new FileInputStream(file));
    }

    public SpecReader(InputStream stream) throws IOException, TraceIOException {
        this.input = new DataInputStream(
                InputStreamFactory.v().getInputStream(stream));
        this.readSpec();
    }
    
    protected void readSpec() throws IOException, TraceIOException {
        this.masks = new int[Event.EVENT_COUNT];
        int magic = this.input.readInt();
        if (magic == Constants.MAGIC) {
            // Found a compiled spec file
            int minor_version = this.input.readShort();
            int major_version = this.input.readShort();
            
            // Sanity check
            if (minor_version != Constants.MINOR_VERSION
                    || major_version != Constants.MAJOR_VERSION) {
                throw new TraceFormatException("Unsupported spec file version: "
                        + major_version + "." + minor_version);
            }
            
            int mask_count = this.input.readInt();
            for (int i = 0; i < mask_count; i++) {
                int id = this.input.readByte();
                int mask = this.input.readInt();
                this.masks[id] = mask;
            }
        } else if (magic == PoolTraceReader.TRACE_MAGIC) {
            // Found a trace file
            PoolTraceReader r = new PoolTraceReader(this.input);
            for (int i = 0; i < Event.EVENT_COUNT; i++) {
                this.masks[i] = r.getFieldMask(i);
            }
        }
    }
    
    public TraceSpecification getTraceSpecification() {
        SpecFormat jvmpi_format = SpecFormatManager.v().getCurrentFormat();
        TraceSpecification trace = new TraceSpecification();
        for (int i = 0; i < this.masks.length; i++) {
            int mask = this.masks[i];
            if ((mask & Constants.FIELD_RECORDED) != 0) {
                // Field is recorded, include it in the trace specification
                
                // Get event definition from JVMPI format
                EventDefinition event_def = jvmpi_format.getByID(i);
                if (event_def == null) {
                    throw new RuntimeException("Unknown event ID: " + i);
                }
                
                // Build event specification
                EventSpecification event_spec
                        = new EventSpecification(event_def);
                FieldDefinition[] fields = event_def.getFields();
                if (fields != null && fields.length > 0) {
                    for (int j = 0; j < fields.length; j++) {
                        FieldDefinition field_def = fields[j];
                        int field_mask = field_def.getMask();
                        if ((mask & field_mask) == field_mask) {
                            // This field is enabled
                            event_spec.add(new FieldSpecification(field_def));
                        }
                    }
                }
                trace.add(event_spec);
            }
        }
        return trace;
    }
}
