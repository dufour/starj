package starj.spec;

import starj.events.Event;

public class SpecFormatManager {
    private static SpecFormatManager instance = null;
    private SpecFormat format;

    private SpecFormatManager() {
        // no instances
     
        this.format = this.buildJVMPISpecFormat();
    }
    
    public SpecFormat getCurrentFormat() {
        return this.format;
    }

    protected SpecFormat buildJVMPISpecFormat() {
        SpecFormat format = new SpecFormat();
        EventDefinition e;
        FieldDefinition f;

        e = new EventDefinition("ArenaDelete", Event.ARENA_DELETE);
        e.addField(new FieldDefinition("arena_id", Constants.FIELD_ARENA_ID));
        e.addField(new FieldDefinition("arena_name", Constants.FIELD_ARENA_NAME));
        format.addEvent(e);

        e = new EventDefinition("ArenaNew", Event.ARENA_NEW);
        e.addField(new FieldDefinition("arena_id", Constants.FIELD_ARENA_ID));
        e.addField(new FieldDefinition("arena_name", Constants.FIELD_ARENA_NAME));
        format.addEvent(e);

        e = new EventDefinition("ClassLoad", Event.CLASS_LOAD);
        e.addField(new FieldDefinition("class_name", Constants.FIELD_CLASS_NAME));
        e.addField(new FieldDefinition("source_name", Constants.FIELD_SOURCE_NAME));
        e.addField(new FieldDefinition("num_interfaces", Constants.FIELD_NUM_INTERFACES));
        f = new FieldDefinition("num_methods", Constants.FIELD_NUM_METHODS);
        e.addField(f);
        e.addField(new FieldDefinition("methods", Constants.FIELD_METHODS, f));
        f = new FieldDefinition("num_static_fields",
                Constants.FIELD_NUM_STATIC_FIELDS);
        e.addField(f);
        e.addField(new FieldDefinition("statics", Constants.FIELD_STATICS, f));
        f = new FieldDefinition("num_instance_fields",
                Constants.FIELD_NUM_INSTANCE_FIELDS);
        e.addField(f);
        e.addField(new FieldDefinition("instances", Constants.FIELD_INSTANCES, f));
        e.addField(new FieldDefinition("class_id", Constants.FIELD_CLASS_LOAD_CLASS_ID));
        format.addEvent(e);

        e = new EventDefinition("ClassLoadHook", Event.CLASS_LOAD_HOOK);
        f = new FieldDefinition("class_data_len", Constants.FIELD_CLASS_DATA_LEN);
        e.addField(f);
        e.addField(new FieldDefinition("class_data", Constants.FIELD_CLASS_DATA, f));

        e = new EventDefinition("ClassUnload", Event.CLASS_UNLOAD);
        e.addField(new FieldDefinition("class_id",
                Constants.FIELD_CLASS_UNLOAD_CLASS_ID));
        format.addEvent(e);

        e = new EventDefinition("CompiledMethodLoad", Event.COMPILED_METHOD_LOAD);
        e.addField(new FieldDefinition("method_id", Constants.FIELD_METHOD_ID));
        f = new FieldDefinition("code_size", Constants.FIELD_CODE_SIZE);
        e.addField(f);
        e.addField(new FieldDefinition("code", Constants.FIELD_CODE, f));
        f = new FieldDefinition("lineno_table_size",
                Constants.FIELD_LINENO_TABLE_SIZE);
        e.addField(f);
        e.addField(new FieldDefinition("lineno_table",
                Constants.FIELD_LINENO_TABLE, f));
        format.addEvent(e);

        e = new EventDefinition("DataDumpRequest", Event.DATA_DUMP_REQUEST);
        format.addEvent(e);

        e = new EventDefinition("DataResetRequest", Event.DATA_RESET_REQUEST);
        format.addEvent(e);

        e = new EventDefinition("GCFinish", Event.GC_FINISH);
        e.addField(new FieldDefinition("used_objects", Constants.FIELD_USED_OBJECTS));
        e.addField(new FieldDefinition("used_object_space",
                Constants.FIELD_USED_OBJECT_SPACE));
        e.addField(new FieldDefinition("total_object_space",
                Constants.FIELD_TOTAL_OBJECT_SPACE));
        format.addEvent(e);

        e = new EventDefinition("GCStart", Event.GC_START);
        format.addEvent(e);

        // FIXME: HeapDump

        e = new EventDefinition("JNIGlobalrefAlloc", Event.JNI_GLOBALREF_ALLOC);
        e.addField(new FieldDefinition("obj_id", Constants.FIELD_OBJ_ID));
        e.addField(new FieldDefinition("ref_id", Constants.FIELD_REF_ID));
        format.addEvent(e);

        e = new EventDefinition("JNIGlobalrefFree", Event.JNI_GLOBALREF_FREE);
        e.addField(new FieldDefinition("ref_id", Constants.FIELD_REF_ID));
        format.addEvent(e);

        e = new EventDefinition("JNIWeakGlobalrefAlloc",
                Event.JNI_WEAK_GLOBALREF_ALLOC);
        e.addField(new FieldDefinition("obj_id", Constants.FIELD_OBJ_ID));
        e.addField(new FieldDefinition("ref_id", Constants.FIELD_REF_ID));
        format.addEvent(e);

        e = new EventDefinition("JNIWeakGlobalrefFree",
                Event.JNI_WEAK_GLOBALREF_FREE);
        e.addField(new FieldDefinition("ref_id", Constants.FIELD_REF_ID));
        format.addEvent(e);

        e = new EventDefinition("JVMInitDone", Event.JVM_INIT_DONE);
        format.addEvent(e);

        e = new EventDefinition("JVMShutDown", Event.JVM_SHUT_DOWN);
        format.addEvent(e);

        e = new EventDefinition("MethodEntry", Event.METHOD_ENTRY);
        e.addField(new FieldDefinition("method_id", Constants.FIELD_METHOD_ID));
        format.addEvent(e);

        e = new EventDefinition("MethodEntry2", Event.METHOD_ENTRY2);
        e.addField(new FieldDefinition("method_id", Constants.FIELD_METHOD_ID));
        e.addField(new FieldDefinition("obj_id", Constants.FIELD_OBJ_ID));
        format.addEvent(e);

        e = new EventDefinition("MethodExit", Event.METHOD_EXIT);
        e.addField(new FieldDefinition("method_id", Constants.FIELD_METHOD_ID));
        format.addEvent(e);

        e = new EventDefinition("MonitorContendedEnter",
                Event.MONITOR_CONTENDED_ENTER);
        e.addField(new FieldDefinition("object", Constants.FIELD_OBJECT));
        format.addEvent(e);

        e = new EventDefinition("MonitorContendedEntered",
                Event.MONITOR_CONTENDED_ENTERED);
        e.addField(new FieldDefinition("object", Constants.FIELD_OBJECT));
        format.addEvent(e);

        e = new EventDefinition("MonitorContendedExit",
                Event.MONITOR_CONTENDED_EXIT);
        e.addField(new FieldDefinition("object", Constants.FIELD_OBJECT));
        format.addEvent(e);

        e = new EventDefinition("MonitorDump", Event.MONITOR_DUMP);
        f = new FieldDefinition("data_len", Constants.FIELD_DATA_LEN);
        e.addField(f);
        e.addField(new FieldDefinition("data", Constants.FIELD_DATA, f));
        f = new FieldDefinition("num_traces", Constants.FIELD_NUM_TRACES);
        e.addField(f);
        e.addField(new FieldDefinition("traces", Constants.FIELD_TRACES, f));
        format.addEvent(e);

        e = new EventDefinition("MonitorWait", Event.MONITOR_WAIT);
        e.addField(new FieldDefinition("object", Constants.FIELD_OBJECT));
        e.addField(new FieldDefinition("timeout", Constants.FIELD_TIMEOUT));
        format.addEvent(e);

        e = new EventDefinition("MonitorWaited", Event.MONITOR_WAITED);
        e.addField(new FieldDefinition("object", Constants.FIELD_OBJECT));
        e.addField(new FieldDefinition("timeout", Constants.FIELD_TIMEOUT));
        format.addEvent(e);

        e = new EventDefinition("ObjectAlloc", Event.OBJECT_ALLOC);
        e.addField(new FieldDefinition("arena_id", Constants.FIELD_ARENA_ID));
        e.addField(new FieldDefinition("obj_id", Constants.FIELD_OBJ_ID));
        e.addField(new FieldDefinition("is_array", Constants.FIELD_IS_ARRAY));
        e.addField(new FieldDefinition("size", Constants.FIELD_SIZE));
        e.addField(new FieldDefinition("class_id",
                Constants.FIELD_OBJECT_ALLOC_CLASS_ID));
        format.addEvent(e);

        e = new EventDefinition("ObjectDump", Event.OBJECT_DUMP);
        f = new FieldDefinition("data_len", Constants.FIELD_DATA_LEN);
        e.addField(f);
        e.addField(new FieldDefinition("data", Constants.FIELD_DATA, f));
        format.addEvent(e);

        e = new EventDefinition("ObjectFree", Event.OBJECT_FREE);
        e.addField(new FieldDefinition("obj_id", Constants.FIELD_OBJ_ID));
        format.addEvent(e);

        e = new EventDefinition("ObjectMove", Event.OBJECT_MOVE);
        e.addField(new FieldDefinition("arena_id", Constants.FIELD_ARENA_ID));
        e.addField(new FieldDefinition("obj_id", Constants.FIELD_OBJ_ID));
        e.addField(new FieldDefinition("new_arena_id", Constants.FIELD_NEW_ARENA_ID));
        e.addField(new FieldDefinition("new_obj_id", Constants.FIELD_NEW_OBJ_ID));
        format.addEvent(e);

        e = new EventDefinition("RawMonitorContendedEnter",
                Event.RAW_MONITOR_CONTENDED_ENTER);
        e.addField(new FieldDefinition("name", Constants.FIELD_NAME));
        e.addField(new FieldDefinition("id", Constants.FIELD_ID));
        format.addEvent(e);

        e = new EventDefinition("RawMonitorContendedEntered",
                Event.RAW_MONITOR_CONTENDED_ENTERED);
        e.addField(new FieldDefinition("name", Constants.FIELD_NAME));
        e.addField(new FieldDefinition("id", Constants.FIELD_ID));
        format.addEvent(e);

        e = new EventDefinition("RawMonitorContendedExit",
                Event.RAW_MONITOR_CONTENDED_EXIT);
        e.addField(new FieldDefinition("name", Constants.FIELD_NAME));
        e.addField(new FieldDefinition("id", Constants.FIELD_ID));
        format.addEvent(e);

        e = new EventDefinition("ThreadEnd", Event.THREAD_END);
        format.addEvent(e);

        e = new EventDefinition("ThreadStart", Event.THREAD_START);
        e.addField(new FieldDefinition("thread_name", Constants.FIELD_THREAD_NAME));
        e.addField(new FieldDefinition("group_name", Constants.FIELD_GROUP_NAME));
        e.addField(new FieldDefinition("parent_name", Constants.FIELD_PARENT_NAME));
        e.addField(new FieldDefinition("thread_id", Constants.FIELD_THREAD_ID));
        e.addField(new FieldDefinition("thread_env_id", Constants.FIELD_THREAD_ENV_ID));
        format.addEvent(e);

        e = new EventDefinition("InstructionStart", Event.INSTRUCTION_START);
        e.addField(new FieldDefinition("method_id", Constants.FIELD_METHOD_ID));
        e.addField(new FieldDefinition("offset", Constants.FIELD_OFFSET));
        e.addField(new FieldDefinition("is_true", Constants.FIELD_IS_TRUE));
        e.addField(new FieldDefinition("key", Constants.FIELD_KEY));
        e.addField(new FieldDefinition("low", Constants.FIELD_LOW));
        e.addField(new FieldDefinition("hi", Constants.FIELD_HI));
        e.addField(new FieldDefinition("chosen_pair_index",
                Constants.FIELD_CHOSEN_PAIR_INDEX));
        e.addField(new FieldDefinition("pairs_total",
                Constants.FIELD_PAIRS_TOTAL));
        format.addEvent(e);

        format.addAll(new FieldDefinition("recorded",
                Constants.FIELD_RECORDED));
        format.addAll(new FieldDefinition("counted", Constants.FIELD_COUNTED));
        format.addAll(new FieldDefinition("env_id", Constants.FIELD_ENV_ID));
        
        return format;
    }

    public static SpecFormatManager v() {
        if (instance == null) {
            instance = new SpecFormatManager();
        }
        return instance;
    }
}
