package starj.toolkits.printers;

import java.io.*;

import starj.*;
import starj.coffer.Code;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;

public class EventPrinter extends AbstractPrinter {
    public final static String[] EVENT_NAMES = {
        "ARENA_DELETE",
        "ARENA_NEW",
        "CLASS_LOAD",
        "CLASS_LOAD_HOOK",
        "CLASS_UNLOAD",
        "COMPILED_METHOD_LOAD",
        "COMPILED_METHOD_UNLOAD",
        "DATA_DUMP_REQUEST",
        "DATA_RESET_REQUEST",
        "GC_FINISH",
        "GC_START",
        "HEAP_DUMP",
        "JNI_GLOBALREF_ALLOC",
        "JNI_GLOBALREF_FREE",
        "JNI_WEAK_GLOBALREF_ALLOC",
        "JNI_WEAK_GLOBALREF_FREE",
        "JVM_INIT_DONE",
        "JVM_SHUT_DOWN",
        "METHOD_ENTRY",
        "METHOD_ENTRY2",
        "METHOD_EXIT",
        "MONITOR_CONTENDED_ENTER",
        "MONITOR_CONTENDED_ENTERED",
        "MONITOR_CONTENDED_EXIT",
        "MONITOR_DUMP",
        "MONITOR_WAIT",
        "MONITOR_WAITED",
        "OBJECT_ALLOC",
        "OBJECT_DUMP",
        "OBJECT_FREE",
        "OBJECT_MOVE",
        "RAW_MONITOR_CONTENDED_ENTER",
        "RAW_MONITOR_CONTENDED_ENTERED",
        "RAW_MONITOR_CONTENDED_EXIT",
        "THREAD_END",
        "THREAD_START",
        "INSTRUCTION_START"
    };

    public EventPrinter(String name, String description) {
        super(name, description);
    }
    
    public EventPrinter(String name, String description, PrintStream out) {
        super(name, description, out);
    }
    
    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();
        
        FieldMask mask = new TotalMask(Constants.FIELD_RECORDED);
        for (int i = 0; i < Event.EVENT_COUNT; i++) {
            dep_set.add(new EventDependency(i, mask, false));
        }

        return dep_set;
    }

    public void apply(EventBox box) {
        Event event = box.getEvent();
        int id = event.getID();

        this.out.print(EVENT_NAMES[id] + " [" + id + "] @ "
                + event.getEnvID() + " {");
        
        switch (id) {
            case Event.ARENA_DELETE: {
                    ArenaDeleteEvent e = (ArenaDeleteEvent) event;
                    this.out.print("id=" + e.getArenaID());
                }
                break;
            case Event.ARENA_NEW: {
                    ArenaNewEvent e = (ArenaNewEvent) event;
                    this.out.print("id=" + e.getArenaID() + ", name="
                            + e.getArenaName());
                }
                break;
            case Event.CLASS_LOAD: {
                    ClassLoadEvent e = (ClassLoadEvent) event;
                    this.out.println("id=" + e.getClassID()
                                 + ", name=" + e.getClassName()
                                 + ", source_name=" + e.getSourceName()
                                 + ", iface_count=" + e.getInterfaceCount());
                    this.out.println("  " + e.getMethodCount() + " methods:");
                    for (int i = 0; i < e.getMethodCount(); i++) {
                        JVMPIMethod m = e.getMethod(i);
                        this.out.println("    [" + m.getMethodID() + "]: "
                                + m.getMethodName() + m.getMethodSignature());
                    }
                }
                break;
            case Event.CLASS_LOAD_HOOK:
                // FIXME
                break;
            case Event.CLASS_UNLOAD: {
                    ClassUnloadEvent e = (ClassUnloadEvent) event;
                    this.out.print("id=" + e.getClassID());
                }
                break;
            case Event.COMPILED_METHOD_LOAD:
            case Event.COMPILED_METHOD_UNLOAD:
            case Event.DATA_DUMP_REQUEST:
            case Event.DATA_RESET_REQUEST:
                // FIXME
                break;
            case Event.GC_FINISH: {
                    GCFinishEvent e = (GCFinishEvent) event;
                    this.out.print("used_objects=" + e.getUsedObjects()
                            + ", used_object_space=" + e.getUsedObjectSpace()
                            + ", total_objectspace=" + e.getTotalObjectSpace());
                }
                break;
            case Event.GC_START:
                // Intentionally empty
                break;
            case Event.HEAP_DUMP:
            case Event.JNI_GLOBALREF_ALLOC:
            case Event.JNI_GLOBALREF_FREE:
            case Event.JNI_WEAK_GLOBALREF_ALLOC:
            case Event.JNI_WEAK_GLOBALREF_FREE:
                // FIXME;
                break;
            case Event.JVM_INIT_DONE:
            case Event.JVM_SHUT_DOWN:
                // Intentionally empty
                break;
            case Event.METHOD_ENTRY:
            case Event.METHOD_EXIT: {
                    MethodEvent e = (MethodEvent) event;
                    this.out.print("id=" + e.getMethodID());
                }
                break;
            case Event.METHOD_ENTRY2: {
                    MethodEntry2Event e = (MethodEntry2Event) event;
                    this.out.print("id=" + e.getMethodID()
                            + ", obj_id=" + e.getObjectID());
                }
                break;
            case Event.MONITOR_CONTENDED_ENTER:
            case Event.MONITOR_CONTENDED_ENTERED:
            case Event.MONITOR_CONTENDED_EXIT: {
                    MonitorEvent e = (MonitorEvent) event;
                    this.out.print("object=" + e.getObjectID());
                }
                break;
            case Event.MONITOR_DUMP:
                // FIXME
                break;
            case Event.MONITOR_WAIT:
            case Event.MONITOR_WAITED: {
                    TimedMonitorEvent e = (TimedMonitorEvent) event;
                    this.out.print("object=" + e.getObjectID()
                            + ", timeout=" + e.getTimeout());
                }
                break;
            case Event.OBJECT_ALLOC: {
                    ObjectAllocEvent e = (ObjectAllocEvent) event;
                    this.out.print("id=" + e.getObjectID()
                            + ", arena_id=" + e.getArenaID()
                            + ", class_id=" + e.getClassID()
                            + ", is_array=" + e.getArrayType()
                            + ", size=" + e.getSize());
                }
                break;
            case Event.OBJECT_DUMP:
                // FIXME
                break;
            case Event.OBJECT_FREE: {
                    ObjectFreeEvent e = (ObjectFreeEvent) event;
                    this.out.print("id=" + e.getObjectID());
                }
                break;
            case Event.OBJECT_MOVE: {
                    ObjectMoveEvent e = (ObjectMoveEvent) event;
                    this.out.print("id=" + e.getObjectID()
                            + ", new_id=" + e.getNewObjectID()
                            + ", arena_id=" + e.getArenaID()
                            + ", new_arena_id" + e.getNewArenaID());
                }
                break;
            case Event.RAW_MONITOR_CONTENDED_ENTER:
            case Event.RAW_MONITOR_CONTENDED_ENTERED:
            case Event.RAW_MONITOR_CONTENDED_EXIT:
                // FIXME
                break;
            case Event.THREAD_END:
                // Intentionally empty
                break;
            case Event.THREAD_START: {
                    ThreadStartEvent e = (ThreadStartEvent) event;
                    this.out.print("id=" + e.getThreadID()
                            + ", thread_name=" + e.getThreadName()
                            + ", group_name=" + e.getGroupName()
                            + ", parent_name=" + e.getParentName()
                            + ", thread_env_id=" + e.getThreadEnvID());
                }
                break;
            case Event.INSTRUCTION_START: {
                    InstructionStartEvent e = (InstructionStartEvent) event;
                    int opcode = e.getOpcode();
                    String opcode_name = Code.getOpcodeName(opcode);
                    if (opcode_name == null) {
                    	opcode_name = "?";
                    }
                    this.out.print("method_id=" + e.getMethodID()
                            + ", offset=" + e.getOffset()
                            + ", opcode=" + opcode_name + "(" + opcode + ")");
                }
                break;
            default:
                break;
        }

        this.out.println("}");
    }
}
