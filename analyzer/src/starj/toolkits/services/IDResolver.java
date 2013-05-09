package starj.toolkits.services;

import java.util.*;

import starj.AbstractOperation;
import starj.EventBox;
import starj.coffer.*;
import starj.dependencies.*;
import starj.events.*;
import starj.io.logging.LogManager;
import starj.spec.Constants;
import starj.coffer.types.*;
import starj.util.IntHashMap;
import starj.util.ObjectToIntHashMap;

public class IDResolver extends AbstractOperation implements Service {
    private static IDResolver instance = new IDResolver();
    
    private IntHashMap id_to_arena_entities;
    private IntHashMap id_to_method_entities;
    private IntHashMap id_to_object_entities;
    private IntHashMap id_to_class_entities;
    private IntHashMap id_to_thread_entities;
    private ObjectToIntHashMap methods_to_ids;

    private IDResolver() {
        super("IDResolver", "Keeps track of entity identifiers");
    }

    public static IDResolver v() {
        return instance;
    }

    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = new EventDependencySet();
        for (int i = 0; i < Event.EVENT_COUNT; i++) {
           dep_set.add(new EventDependency(i,
                   new TotalMask(Constants.FIELD_RECORDED),
                   false));
        }
        /*
        dep_set.add(new EventDependency(
                Event.CLASS_LOAD,
                new TotalMask(
                        Constants.FIELD_RECORDED
                        | Constants.FIELD_CLASS_LOAD_CLASS_ID),
                false));
        dep_set.add(new EventDependency(
                Event.CLASS_UNLOAD,
                new TotalMask(
                        Constants.FIELD_RECORDED
                        | Constants.FIELD_CLASS_UNLOAD_CLASS_ID),
                false));
        dep_set.add(new EventDependency(
                Event.OBJECT_ALLOC,
                new TotalMask(
                        Constants.FIELD_RECORDED
                        | Constants.FIELD_OBJ_ID),
                false));
        dep_set.add(new EventDependency(
                Event.OBJECT_MOVE,
                new TotalMask(
                        Constants.FIELD_RECORDED
                        | Constants.FIELD_OBJ_ID
                        | Constants.FIELD_NEW_OBJ_ID),
                false));
        dep_set.add(new EventDependency(
                Event.OBJECT_FREE,
                new TotalMask(
                        Constants.FIELD_RECORDED
                        | Constants.FIELD_OBJ_ID),
                false));
        dep_set.add(new EventDependency(
                Event.ARENA_NEW,
                new TotalMask(
                        Constants.FIELD_RECORDED
                        | Constants.FIELD_ARENA_ID),
                false));
        dep_set.add(new EventDependency(
                Event.ARENA_DELETE,
                new TotalMask(
                        Constants.FIELD_RECORDED
                        | Constants.FIELD_ARENA_ID),
                false));
        dep_set.add(new EventDependency(
                Event.THREAD_START,
                new TotalMask(
                        Constants.FIELD_RECORDED
                        | Constants.FIELD_ARENA_ID),
                false));
        */
        return dep_set;
    }

    public void init() {
        this.id_to_arena_entities = new IntHashMap(7);
        this.id_to_method_entities = new IntHashMap(1023);
        this.id_to_object_entities = new IntHashMap(1023);
        this.id_to_class_entities = new IntHashMap(1023);
        this.id_to_thread_entities = new IntHashMap(15);
        this.methods_to_ids = new ObjectToIntHashMap(1023);
    }

    public void apply(EventBox box) {
         Event event = box.getEvent();
         if (!this.id_to_thread_entities.containsKey(event.getEnvID())) {
             // Add temporary thread entity object
             this.id_to_thread_entities.put(event.getEnvID(), 
                     new ThreadEntity(event.getEnvID(),
                             null, // Object entity
                             null, // Thread name
                             null, // Parent name
                             null, // Group name
                             false // Authoritative
                     )
             );
         }

         switch (event.getID()) {
            case Event.CLASS_LOAD: {
                    ClassLoadEvent e = (ClassLoadEvent) event;

                    int class_id = e.getClassID();
                    boolean requested = e.isRequested();
                    
                    ClassEntity class_entity = this.getClassEntity(class_id);
                    if (class_entity != null) {
                        if (requested) {
                            // This event was requested, and thus should not
                            // overwrite the previous information
                            box.remove();
                        }

                        if (!class_entity.isAuthoritative()) {
                            // All previous occurences of this event were
                            // requested ones, so this is the first 'real'
                            // occurence. Do not overwrite.
                            class_entity.stepAuthoritativeCount();
                            box.remove();
                        }
                    }

                    // Get ClassFile instance for loaded class
                    String class_name = e.getClassName();
                    ClassFile class_file = Repository.v().lookup(class_name);

                    // Prepare method array
                    MethodEntity[] method_entities = null;
                    JVMPIMethod[] methods = e.getMethods();
                    if (methods != null) {
                        // Create an empty array for now, we will fill in
                        // the values later (the MethodEntity constructor
                        // needs a ClassEntity reference)
                        method_entities = new MethodEntity[methods.length];
                    }

                    // Perform mapping
                    ObjectEntity obj_entity = this.getObjectEntity(class_id);
                    if (obj_entity == null) {
                        // FIXME
                        //if (Scene.v().isTypeDumped(AdaptJEvent.ADAPTJ_OBJECT_ALLOC)) {
                        //    LogManager.v().showWarning("No prior Class object defined");
                        //}
                        
                        obj_entity = new ObjectEntity(class_id,
                            null, // Arena
                            TypeRepository.v().getClassType("java.lang.Class"),
                            -1,   // Size
                            false // Authoritative
                        );
                        this.id_to_object_entities.put(class_id, obj_entity);
                    }
                    class_entity = new ClassEntity(
                        e.getClassID(),
                        obj_entity,
                        class_file,
                        class_name,
                        e.getSourceName(),
                        e.getInterfaceCount(),
                        method_entities,
                        TypeRepository.v().getClassType(class_name),
                        !requested
                    );

                    this.id_to_class_entities.put(class_id, class_entity);
                    
                    // Now that we have a ClassEntity instance, we can
                    // build all MethodEntity objects
                    if (methods != null) {
                        Map method_map = new HashMap();
                        if (class_file != null) {
                            Method[] cf_methods = class_file.getMethods();
                            if (cf_methods.length != methods.length) {
                                // Class files differ!
                                throw new RuntimeException("Class files for "
                                        + "class '" + class_name + "' differ");
                                        
                            }
                            for (int i = 0; i < methods.length; i++) {
                                Method m = cf_methods[i];
                                method_map.put(m.getFullName(), m);
                            }
                        } else if (class_name != null
                                && class_name.charAt(0) != '[') {
                            LogManager.v().logWarning("Failed to locate class: "
                                    + e.getClassName());
                        }
                        
                        for (int i = 0; i < methods.length; i++) {
                            JVMPIMethod m = methods[i];
                            Method cf_m = (Method) method_map.get(
                                    m.getMethodName() + m.getMethodSignature());
                            MethodEntity method_entity = new MethodEntity(
                                class_entity,
                                m,
                                cf_m,
                                !requested
                            );
                            method_entities[i] = method_entity;
                            int method_id = method_entity.getID();
                            this.id_to_method_entities.put(method_id,
                                    method_entity);
                            this.methods_to_ids.put(cf_m, method_id);
                        }
                    }
                }
                break;
            case Event.CLASS_UNLOAD: {
                    ClassUnloadEvent e = (ClassUnloadEvent) event;
                    int class_id = e.getClassID();
                    this.releaseClass(class_id);
                }
                break;
            case Event.OBJECT_ALLOC: {
                     ObjectAllocEvent e = (ObjectAllocEvent) event;

                    int obj_id = e.getObjectID();
                    boolean requested = e.isRequested();

                    ObjectEntity obj_entity = this.getObjectEntity(obj_id);
                    if (obj_entity != null) {
                        if (requested) {
                            // This event was requested, and thus should not
                            // overwrite the previous information
                            box.remove();
                        }

                        if (!obj_entity.isAuthoritative()) {
                            // All previous occurences of this event were
                            // requested ones, so this is the first 'real'
                            // occurence. Do not overwrite.
                            obj_entity.stepAuthoritativeCount();
                            box.remove();
                        }
                    }

                    int arena_id = e.getArenaID();
                    ArenaEntity arena = this.getArenaEntity(arena_id);
                    if (arena_id != 0 && arena == null) {
                        arena = new ArenaEntity(arena_id, null, false);
                        this.id_to_arena_entities.put(arena_id, arena);
                    }

                    Type type = null;
                    switch (e.getArrayType()) {
                        case ObjectAllocEvent.NORMAL_OBJECT: {
                                int class_id = e.getClassID();
                                ClassEntity class_entity
                                        = this.getClassEntity(class_id);
                                if (class_entity != null) {
                                    type = class_entity.getType();
                                }
                            }
                            break;
                        case ObjectAllocEvent.OBJECT_ARRAY: {
                                int class_id = e.getClassID();
                                ClassEntity class_entity
                                        = this.getClassEntity(class_id);
                                if (class_entity != null) {
                                    type = new ArrayType(class_entity.getType());
                                }
                            }
                            break;
                        case ObjectAllocEvent.BOOLEAN_ARRAY:
                            type = TypeRepository.BOOL_ARRAY_TYPE;
                            break;
                        case ObjectAllocEvent.CHAR_ARRAY:
                            type = TypeRepository.CHAR_ARRAY_TYPE;
                            break;
                        case ObjectAllocEvent.FLOAT_ARRAY:
                            type = TypeRepository.FLOAT_ARRAY_TYPE;
                            break;
                        case ObjectAllocEvent.DOUBLE_ARRAY:
                            type = TypeRepository.DOUBLE_ARRAY_TYPE;
                            break;
                        case ObjectAllocEvent.BYTE_ARRAY:
                            type = TypeRepository.BYTE_ARRAY_TYPE;
                            break;
                        case ObjectAllocEvent.SHORT_ARRAY:
                            type = TypeRepository.SHORT_ARRAY_TYPE;
                            break;
                        case ObjectAllocEvent.INT_ARRAY:
                            type = TypeRepository.INT_ARRAY_TYPE;
                            break;
                        case ObjectAllocEvent.LONG_ARRAY:
                            type = TypeRepository.LONG_ARRAY_TYPE;
                            break;
                        default:
                            throw new RuntimeException("Invalid object type: "
                                    + e.getArrayType());
                    }

                    obj_entity = new ObjectEntity(
                        obj_id,
                        arena,
                        type, // FIXME
                        e.getSize(),
                        !requested
                    );
                    
                    // Make sure we don't leak ObjectEntity instances
                    this.releaseObject(obj_id);

                    // Add the new object to its arena
                    if (arena != null) {
                        arena.add(obj_entity);
                    }
                    this.id_to_object_entities.put(obj_id, obj_entity);
                }
                break;
            case Event.OBJECT_MOVE: {
                    ObjectMoveEvent e = (ObjectMoveEvent) event;
                    
                    int obj_id = e.getObjectID();
                    int new_obj_id = e.getNewObjectID();

                    ObjectEntity obj_entity = this.getObjectEntity(obj_id);
                    if (obj_entity != null) {
                        obj_entity.moveTo(new_obj_id,
                                this.getArenaEntity(e.getNewArenaID()));
                        this.id_to_object_entities.remove(obj_id);
                        this.id_to_object_entities.put(new_obj_id, obj_entity);

                        ClassEntity c = this.getClassEntity(obj_id);
                        if (c != null) {
                            this.id_to_class_entities.remove(obj_id);
                            c.moveTo(new_obj_id);
                            this.id_to_class_entities.put(new_obj_id, c);
                        }
                    }
                }
                break;
            case Event.OBJECT_FREE: {
                    ObjectFreeEvent e = (ObjectFreeEvent) event;
                    int obj_id = e.getObjectID();
                    this.releaseObject(obj_id);
                }
                break;
            case Event.ARENA_NEW: {
                    ArenaNewEvent e = (ArenaNewEvent) event;
                    
                    int arena_id = e.getArenaID();
                    this.id_to_arena_entities.put(arena_id,
                            new ArenaEntity(arena_id, e.getArenaName()));
                }
                break;
            case Event.ARENA_DELETE: {
                    ArenaDeleteEvent e = (ArenaDeleteEvent) event;
                    int arena_id = e.getArenaID();
                    ArenaEntity arena_entity = (ArenaEntity)
                            this.id_to_arena_entities.remove(arena_id);
                    if (arena_entity != null) {
                        Set s = arena_entity.getObjects();
                        for (Iterator i = s.iterator(); i.hasNext(); ) {
                            ObjectEntity obj_entity = (ObjectEntity) i.next();
                            int obj_id = obj_entity.getID();
                            this.releaseObject(obj_id);
                        }
                    }
                }
                break;
            case Event.THREAD_START: {
                    ThreadStartEvent e = (ThreadStartEvent) event;
                    int env_id = e.getThreadEnvID();
                    
                    // Locate this thread's object
                    int thread_object_id = e.getID();
                    ObjectEntity thread_object
                            = this.getObjectEntity(thread_object_id);
                    if (thread_object == null && thread_object_id > 0) {
                        // FIXME: if object allac availabe, show warning
                        thread_object = new ObjectEntity(thread_object_id,
                            null, // Arena
                            TypeRepository.v().getClassType("java.lang.Thread"),
                            -1,   // Size
                            false // Authoritative
                        );
                        this.id_to_object_entities.put(thread_object_id,
                                thread_object);
                    } // else: no thread ID supplied (null entity kept)
                    
                    ThreadEntity thread_entity = this.getThreadEntity(env_id);
                    // Look for a current thread entity (parent thread of the
                    // newly created one)
                    ThreadEntity parent_thread = this.getThreadEntity(
                            e.getEnvID());
                    if (parent_thread == thread_entity) { 
                        // Make sure that we don't consider this thread to be
                        //   its parent during VM initialization
                        parent_thread = null;
                    }
                    
                    if (thread_entity == null) {
                        thread_entity = new ThreadEntity(env_id, thread_object,
                                e.getThreadName(), e.getParentName(),
                                e.getGroupName(), parent_thread,
                                !e.isRequested());
                    } else {
                        thread_entity.setThreadName(e.getThreadName());
                        thread_entity.setParentName(e.getParentName());
                        thread_entity.setGroupName(e.getGroupName());
                        thread_entity.setParent(parent_thread);
                        thread_entity.stepAuthoritativeCount();
                    }
                }
                break;
            case Event.THREAD_END: {
                    ThreadEndEvent e = (ThreadEndEvent) event;
                    this.id_to_thread_entities.remove(e.getEnvID());
                }
                break;
            default:
                // Should not happen since we did not request other events
                break;
         }
    }

    private void releaseClass(int class_id) {
        ClassEntity class_entity = this.getClassEntity(class_id);
        if (class_entity != null) {
            MethodEntity[] methods = class_entity.getMethods();
            if (methods != null) {
                for (int i = 0; i < methods.length; i++) {
                    MethodEntity m = methods[i];
                    this.id_to_method_entities.remove(m.getID());
                    methods[i] = null; // For GC purposes
                }
            }
        }
        this.id_to_class_entities.remove(class_id);
    }

    private void releaseObject(int obj_id) {
        ObjectEntity entity = this.getObjectEntity(obj_id);
        if (entity != null) {
            ArenaEntity arena = entity.getArena();
            if (arena != null) {
                arena.remove(entity);
            }
            this.id_to_object_entities.remove(obj_id);
        }
        this.releaseClass(obj_id); // Also release any corresponding class
    }

    public ClassEntity getClassEntity(int class_id) {
        return (ClassEntity) this.id_to_class_entities.get(class_id);
    }

    public ObjectEntity getObjectEntity(int object_id) {
        return (ObjectEntity) this.id_to_object_entities.get(object_id);
    }

    public MethodEntity getMethodEntity(int method_id) {
        return (MethodEntity) this.id_to_method_entities.get(method_id);
    }

    public ArenaEntity getArenaEntity(int arena_id) {
        return (ArenaEntity) this.id_to_arena_entities.get(arena_id);
    }

    public ThreadEntity getThreadEntity(int env_id) {
        return (ThreadEntity) this.id_to_thread_entities.get(env_id);
    }
    
    public int getMethodID(Method method) {
        return this.methods_to_ids.get(method);
    }
}
