default {
    recorded: yes;
    env_id: yes;
}

event Arena* {
    arena_id: yes;
}

event Class* {
    class_id: yes;
}

event ClassLoad {
    class_name: yes;
    source_name: yes;
    methods: yes;
}

event JVM* {
}

event MethodEntry2 {
    method_id: yes;
    obj_id: yes;
}

event MethodExit {
    method_id: yes;
}

event ObjectAlloc {
    arena_id: yes;
    size: yes;
    is_array: yes;
    class_id: yes;
    obj_id: yes;
}

event GC* {
}

event MonitorContended* {
    object: yes;
}

event ThreadStart {
    thread_id: yes;
    thread_env_id: yes;
}

event ThreadEnd {
}

event ObjectFree {
    obj_id: yes;
}

event ObjectMove {
    obj_id: yes;
    arena_id: yes;
    new_obj_id: yes;
    new_arena_id: yes;
}

event InstructionStart {
    method_id: yes;
    offset: yes;
}
