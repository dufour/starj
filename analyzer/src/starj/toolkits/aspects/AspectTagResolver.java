package starj.toolkits.aspects;

import starj.EventBox;
import starj.coffer.*;
import starj.dependencies.*;
import starj.events.*;
import starj.spec.Constants;
import starj.toolkits.services.*;
import starj.util.IntHashMap;
import starj.util.IntStack;

public class AspectTagResolver extends PropagationOperation implements Service {
    private static AspectTagResolver instance = new AspectTagResolver();

    public static final int ASPECT_TAG_NO_TAG                       = -1;
    public static final int ASPECT_TAG_BASE_CODE                    = 0;
    public static final int ASPECT_TAG_ADVICE_EXECUTE               = 1;
    public static final int ASPECT_TAG_ADVICE_ARG_SETUP             = 2;
    public static final int ASPECT_TAG_ADVICE_TEST                  = 3;
    public static final int ASPECT_TAG_AFTER_THROWING_HANDLER       = 4;
    public static final int ASPECT_TAG_EXCEPTION_SOFTENER           = 5;
    public static final int ASPECT_TAG_AFTER_RETURNING_EXPOSURE     = 6;
    public static final int ASPECT_TAG_PEROBJECT_ENTRY              = 7;
    public static final int ASPECT_TAG_CFLOW_EXIT                   = 8;
    public static final int ASPECT_TAG_CFLOW_ENTRY                  = 9;
    public static final int ASPECT_TAG_PRIV_METHOD                  = 10;
    public static final int ASPECT_TAG_PRIV_FIELD_GET               = 11;
    public static final int ASPECT_TAG_PRIV_FIELD_SET               = 12;
    public static final int ASPECT_TAG_CLINIT                       = 13;
    public static final int ASPECT_TAG_INTERMETHOD                  = 14;
    public static final int ASPECT_TAG_INTERFIELD_GET               = 15;
    public static final int ASPECT_TAG_INTERFIELD_SET               = 16;
    public static final int ASPECT_TAG_INTERFIELD_INIT              = 17;
    public static final int ASPECT_TAG_INTERCONSTRUCTOR_PRE         = 18;
    public static final int ASPECT_TAG_INTERCONSTRUCTOR_POST        = 19;
    public static final int ASPECT_TAG_INTERCONSTRUCTOR_CONVERSION  = 20;
    public static final int ASPECT_TAG_PEROBJECT_SET                = 21;
    public static final int ASPECT_TAG_PEROBJECT_GET                = 22;
    public static final int ASPECT_TAG_AROUND_CONVERSION            = 23;
    public static final int ASPECT_TAG_AROUND_CALLBACK              = 24;
    public static final int ASPECT_TAG_AROUND_PROCEED               = 25;
    public static final int ASPECT_TAG_CLOSURE_INIT                 = 26;
    public static final int ASPECT_TAG_INLINE_ACCESS_METHOD         = 27;
    public static final int ASPECT_TAG_ASPECT_CODE                  = 28;

    public static final int[] PROPAGATION_TABLE = {
        ASPECT_TAG_BASE_CODE,                    // BASE_CODE                   
        ASPECT_TAG_ASPECT_CODE,                  // ADVICE_EXECUTE              
        ASPECT_TAG_ADVICE_ARG_SETUP,             // ADVICE_ARG_SETUP            
        ASPECT_TAG_ADVICE_TEST,                  // ADVICE_TEST                 
        ASPECT_TAG_AFTER_THROWING_HANDLER,       // AFTER_THROWING_HANDLER      
        ASPECT_TAG_EXCEPTION_SOFTENER,           // EXCEPTION_SOFTENER          
        ASPECT_TAG_AFTER_RETURNING_EXPOSURE,     // AFTER_RETURNING_EXPOSURE    
        ASPECT_TAG_PEROBJECT_ENTRY,              // PEROBJECT_ENTRY             
        ASPECT_TAG_CFLOW_EXIT,                   // CFLOW_EXIT                  
        ASPECT_TAG_CFLOW_ENTRY,                  // CFLOW_ENTRY                 
        ASPECT_TAG_PRIV_METHOD,                  // PRIV_METHOD                 
        ASPECT_TAG_PRIV_FIELD_GET,               // PRIV_FIELD_GET              
        ASPECT_TAG_PRIV_FIELD_SET,               // PRIV_FIELD_SET              
        ASPECT_TAG_CLINIT,                       // CLINIT                      
        ASPECT_TAG_ASPECT_CODE,                  // INTERMETHOD                 
        ASPECT_TAG_INTERFIELD_GET,               // INTERFIELD_GET              
        ASPECT_TAG_INTERFIELD_SET,               // INTERFIELD_SET              
        ASPECT_TAG_INTERFIELD_INIT,              // INTERFIELD_INIT             
        ASPECT_TAG_INTERCONSTRUCTOR_PRE,         // INTERCONSTRUCTOR_PRE        
        ASPECT_TAG_INTERCONSTRUCTOR_POST,        // INTERCONSTRUCTOR_POST       
        ASPECT_TAG_INTERCONSTRUCTOR_CONVERSION,  // INTERCONSTRUCTOR_CONVERSION
        ASPECT_TAG_PEROBJECT_SET,                // PEROBJECT_SET               
        ASPECT_TAG_PEROBJECT_GET,                // PEROBJECT_GET               
        ASPECT_TAG_AROUND_CONVERSION,            // AROUND_CONVERSION           
        ASPECT_TAG_BASE_CODE,                    // AROUND_CALLBACK             
        ASPECT_TAG_BASE_CODE,                    // AROUND_PROCEED              
        ASPECT_TAG_CLOSURE_INIT,                 // CLOSURE_INIT                
        ASPECT_TAG_ASPECT_CODE,                  // INLINE_ACCESS_METHOD        
        ASPECT_TAG_ASPECT_CODE                   // ASPECT_CODE                 
    };

    // REPLACEMENT_TABLE[current][propagated] = new
    public static final int[][] REPLACEMENT_TABLE;

    /* IMPORTANT NOTE: the constants above are *required* to
     * go from 0 to ASPECT_TAG_COUNT - 1 */
    public static final int ASPECT_TAG_COUNT = PROPAGATION_TABLE.length;
    
    private static final TagEntity[] TAG_ENTITIES;
    private static final TagEntity NO_TAG_ENTITY
            = new TagEntity(ASPECT_TAG_NO_TAG);

    static {
        int[][] repl_tbl = new int[ASPECT_TAG_COUNT][ASPECT_TAG_COUNT];

        // Fill the table assuming that no tag can be overriden
        for (int i = 0; i < ASPECT_TAG_COUNT; i++) {
            for (int j = 0; j < ASPECT_TAG_COUNT; j++) {
                repl_tbl[i][j] = i;
            }
        }

        // Customization of the replacement table
        repl_tbl[ASPECT_TAG_BASE_CODE][ASPECT_TAG_ASPECT_CODE]
                = ASPECT_TAG_ASPECT_CODE;
        for (int i = 0; i < ASPECT_TAG_COUNT; i++) {
            if (PROPAGATION_TABLE[i] == i) {
                // The tag is propagated. Allow it to override BASE_CODE
                // and ASPECT_CODE
                repl_tbl[ASPECT_TAG_BASE_CODE][i] = i;
                repl_tbl[ASPECT_TAG_ASPECT_CODE][i] = i;
            }
        }

        REPLACEMENT_TABLE = repl_tbl;
        
        TAG_ENTITIES = new TagEntity[ASPECT_TAG_COUNT];
        for (int i = 0; i < ASPECT_TAG_COUNT; i++) {
            TAG_ENTITIES[i] = new TagEntity(i);
        }
    }

    public static final String[] TAG_NAMES = {
        "BASE_CODE",                      // BASE_CODE                   
        "ADVICE_EXECUTE",                 // ADVICE_EXECUTE              
        "ADVICE_ARG_SETUP",               // ADVICE_ARG_SETUP            
        "ADVICE_TEST",                    // ADVICE_TEST                 
        "AFTER_THROWING_HANDLER",         // AFTER_THROWING_HANDLER      
        "EXCEPTION_SOFTENER",             // EXCEPTION_SOFTENER          
        "AFTER_RETURNING_EXPOSURE",       // AFTER_RETURNING_EXPOSURE    
        "PEROBJECT_ENTRY",                // PEROBJECT_ENTRY             
        "CFLOW_EXIT",                     // CFLOW_EXIT                  
        "CFLOW_ENTRY",                    // CFLOW_ENTRY                 
        "PRIV_METHOD",                    // PRIV_METHOD                 
        "PRIV_FIELD_GET",                 // PRIV_FIELD_GET              
        "PRIV_FIELD_SET",                 // PRIV_FIELD_SET              
        "CLINIT",                         // CLINIT                      
        "INTERMETHOD",                    // INTERMETHOD                 
        "INTERFIELD_GET",                 // INTERFIELD_GET              
        "INTERFIELD_SET",                 // INTERFIELD_SET              
        "INTERFIELD_INIT",                // INTERFIELD_INIT             
        "INTERCONSTRUCTOR_PRE",           // INTERCONSTRUCTOR_PRE        
        "INTERCONSTRUCTOR_POST",          // INTERCONSTRUCTOR_POST       
        "INTERCONSTRUCTOR_CONVERSION",    // INTERCONSTRUCTOR_CONVERSION
        "PEROBJECT_SET",                  // PEROBJECT_SET               
        "PEROBJECT_GET",                  // PEROBJECT_GET               
        "AROUND_CONVERSION",              // AROUND_CONVERSION           
        "AROUND_CALLBACK",                // AROUND_CALLBACK             
        "AROUND_PROCEED",                 // AROUND_PROCEED              
        "CLOSURE_INIT",                   // CLOSURE_INIT                
        "INLINE_ACCESS_METHOD",           // INLINE_ACCESS_METHOD        
        "ASPECT_CODE"                     // ASPECT_CODE                 
    };
    
    private int static_tag_index;
    private int dynamic_tags_index;
    private IntHashMap env_id_to_depth_stacks;

    private AspectTagResolver() {
        super("AspectTagResolver",
                "Manages and propagates tags produced by cajc");
    }

    int getAspectCodeDepth(int env_id) {
        IntStack aspect_code_depth_stack
                = (IntStack) this.env_id_to_depth_stacks.get(env_id);
        if (aspect_code_depth_stack != null) {
            return aspect_code_depth_stack.top(0);
        }

        return 0;
    }

    public static AspectTagResolver v() {
        return AspectTagResolver.instance;
    }

    public OperationSet operationDependencies() {
        OperationSet dep_set = super.operationDependencies();
        dep_set.add(CallSiteResolver.v());

        return dep_set;
    }

    public EventDependencySet eventDependencies() {
        EventDependencySet dep_set = super.eventDependencies();
        dep_set.add(new EventDependency(Event.CLASS_LOAD,
                new TotalMask(
                        Constants.FIELD_RECORDED
                        | Constants.FIELD_CLASS_LOAD_CLASS_ID
                        | Constants.FIELD_METHODS),
                true));
        
        return dep_set;
    }

    public void init() {
        super.init();
        AspectTagAttributeRegistrar.registerAttributes();
        this.static_tag_index = Instruction.registerStorageSpace();
        this.dynamic_tags_index = Instruction.registerStorageSpace();
        this.env_id_to_depth_stacks = new IntHashMap(7);
    }

    public void apply(EventBox box) {
        Event event = box.getEvent();
        if (event.getID() != Event.CLASS_LOAD) {
            super.apply(box);
            return;
        }
        
        ClassLoadEvent e = (ClassLoadEvent) box.getEvent();

        /* Look at each method in the class, and tag the bytecodes
         * appropriately */
        int class_id = e.getClassID(); 
        ClassEntity class_entity
                = IDResolver.v().getClassEntity(class_id);
        
        MethodEntity[] methods = class_entity.getMethods();
        if (methods != null) {
            for (int j = 0; j < methods.length; j++) {
                MethodEntity me = methods[j];
                Method m = me.getMethod();
                if (m != null) {
                    Code code = m.getCode();
                    if (code != null) {
                        Attributes method_attribs = m.getAttributes();
                        CodeAttribute code_attrib
                                = (CodeAttribute) method_attribs.lookupFirst(
                                    CodeAttribute.ATTRIBUTE_NAME);
                        if (code_attrib != null) {
                            this.processCode(code, code_attrib.getAttributes());
                        }
                    }
                }
            }
        }
    }
    
    private void processCode(Code code, Attributes attribs) {
        AspectTagAttribute kind_tag_attrib = (AspectKindTagAttribute)
                attribs.lookupFirst(AspectKindTagAttribute.ATTRIBUTE_NAME);
        AspectTagAttribute source_tag_attrib = (AspectSourceTagAttribute)
                attribs.lookupFirst(AspectSourceTagAttribute.ATTRIBUTE_NAME);
        AspectTagAttribute shadow_tag_attrib = (AspectShadowTagAttribute)
                attribs.lookupFirst(AspectShadowTagAttribute.ATTRIBUTE_NAME);
        
        Instruction[] insts = code.getInstructions();
        if (kind_tag_attrib != null) {
            for (int i = 0; i < insts.length; i++) {
                Instruction inst = insts[i];
                int kind_tag = kind_tag_attrib.getTag(inst.getOffset());
                TagEntity tag_entity;
                if (kind_tag != ASPECT_TAG_NO_TAG) {
                    tag_entity = TAG_ENTITIES[kind_tag];
                } else {
                    tag_entity = NO_TAG_ENTITY;
                }
                inst.setStorageSpace(this.static_tag_index, tag_entity);
            }
        } else {
            for (int i = 0; i < insts.length; i++) {
                Instruction inst = insts[i];
                inst.setStorageSpace(this.static_tag_index, NO_TAG_ENTITY);
            }
        }
        
        // FIXME: shadow/source tags
    }
    
    public int getStaticKindTag(Instruction inst) {
        return this.getStaticKindTag(inst, ASPECT_TAG_NO_TAG);
    }
    
    public int getStaticKindTag(Instruction inst, int default_tag) {
        TagEntity tag = (TagEntity) inst.getStorageSpace(this.static_tag_index);
        if (tag != null) {
            return tag.getTag();
        }
        
        return default_tag;
    }
    
    /*
    public int getCurrentKindTag(Instruction inst, int env_id) {
        return this.getCurrentKindTag(inst, env_id, ASPECT_TAG_NO_TAG);
    }
    */

    public int getCurrentKindTag(Instruction inst, int env_id) {
        /*
        IntHashMap stack_map = (IntHashMap)
                inst.getStorageSpace(this.dynamic_tags_index);
        if (stack_map != null) {
            IntStack stack = (IntStack) stack_map.get(env_id);
            if (stack != null) {
                return stack.top();
            }
        }
        
        return default_tag;
        */
        IntStack tag_stack = this.getKindTagStack(inst, env_id);
        return tag_stack.top();
    }
    
    private IntStack getKindTagStack(Instruction inst, int env_id) {
        // Get (env id -> tag stack) map
        IntHashMap stack_map = (IntHashMap) inst.getStorageSpace(
                this.dynamic_tags_index);
        if (stack_map == null) {
            stack_map = new IntHashMap(7);
            inst.setStorageSpace(this.dynamic_tags_index, stack_map);
        }
        
        // Lookup stack using env ID of call
        IntStack tags_stack = (IntStack) stack_map.get(env_id);
        if (tags_stack == null) {
            // Create a new stack
            tags_stack = new IntStack();
            tags_stack.push(this.getStaticKindTag(inst));
            stack_map.put(env_id, tags_stack);
        }

        return tags_stack;
    }
    
    private static class TagEntity {
        private int tag;
        
        public TagEntity(int tag) {
            this.tag = tag;
        }
        
        public int getTag() {
            return this.tag;
        }
    }

    /*
    private class AspectKindTagPropagation implements Propagation {
        private AspectTagResolver resolver;
        private int static_tag_index;
        private int dynamic_tags_index;
        
        public AspectKindTagPropagation(int static_tag_index,
                int dynamic_tags_index) {
            this.resolver = AspectTagResolver.this;
            this.static_tag_index = static_tag_index;
            this.dynamic_tags_index = dynamic_tags_index;
        }
        
    }
    */


    public void propagate(InvokeInstruction call_site,
            MethodEntity new_method, ExecutionContext call_context) {
        ThreadEntity thread = call_context.getThread();
        int env_id = thread.getID();
        Method m = new_method.getMethod();
        if (m == null) {
            // FIXME: log warning?
            return;
        }
        
        // Get the aspect code depth stack for this thread
        IntStack aspect_code_depth_stack
                = (IntStack) this.env_id_to_depth_stacks.get(env_id);
        if (aspect_code_depth_stack == null) {
            aspect_code_depth_stack = new IntStack();
            this.env_id_to_depth_stacks.put(env_id,
                    aspect_code_depth_stack);
        }
        
        if (call_site != null) {
            // Get the current tag of the call site instruction
            int tag = this.getCurrentKindTag(call_site, env_id);
            
            int current_depth = aspect_code_depth_stack.top(0);

            // Compute the progated tag
            int propagated_tag;
            switch (tag) {
                case ASPECT_TAG_NO_TAG:
                    // Can't have a negative index into propagation table,
                    // so we must treat this case specially
                    propagated_tag = ASPECT_TAG_BASE_CODE;
                    break;
                case ASPECT_TAG_AROUND_CALLBACK:
                case ASPECT_TAG_AROUND_PROCEED:
                    // Special case: normally we propagate BASE_CODE, we
                    // need to propagate ASPECT_CODE if the proceed call
                    // actually invokes aspect code (i.e. when
                    // current_depth > 1)
                    if (current_depth > 1) {
                        propagated_tag = ASPECT_TAG_ASPECT_CODE;
                    } else {
                        propagated_tag = ASPECT_TAG_BASE_CODE;
                    }
                    break;
                default:
                    propagated_tag = PROPAGATION_TABLE[tag];
                    break;
            }

            // Adjust the aspect code depth
            if (tag == ASPECT_TAG_ADVICE_EXECUTE) {
                current_depth += 1;
            } else {
                Attributes attribs = m.getAttributes();
                if (attribs.lookupFirst(
                        AspectProceedMethodAttribute.ATTRIBUTE_NAME)
                        != null) {
                    current_depth -= 1;
                } 
            }

            aspect_code_depth_stack.push(current_depth);
            Code c = m.getCode();
            if (c != null) {
                Instruction[] insts = c.getInstructions();
                for (int i = 0; i < insts.length; i++) {
                    Instruction inst = insts[i];
                    int curr_tag = this.getCurrentKindTag(inst, env_id);
                    if (curr_tag == ASPECT_TAG_NO_TAG) {
                        curr_tag = ASPECT_TAG_BASE_CODE;
                    }
                    
                    int new_tag;
                    if (propagated_tag == ASPECT_TAG_BASE_CODE
                            && this.getStaticKindTag(inst)
                                    == ASPECT_TAG_ASPECT_CODE) {
                        // BASE_CODE propagated over a *static* ASPECT_CODE tag.
                        // This is highly improbable (if at all possible), but
                        // it is not really difficult to make sure that if this
                        // situation ever occurs, we will handle it correclty.
                        //
                        // The meaning of a statically assigned ASPECT_CODE tag
                        // is such that ASPECT_CODE should be propagated here
                        // instead
                        new_tag = ASPECT_TAG_ASPECT_CODE;
                    } else {
                        new_tag = REPLACEMENT_TABLE[curr_tag][propagated_tag];
                    }
                    this.pushTag(new_tag, env_id, inst);
                }
            }
            
        } else {
            // We have no call site, so just push the static
            // tags for all instructions
            Code c = m.getCode();
            if (c != null) {
                Instruction[] insts = c.getInstructions();
                for (int i = 0; i < insts.length; i++) {
                    Instruction inst = insts[i];
                    int tag = this.getStaticKindTag(inst);
                    this.pushTag(tag, env_id, inst);
                }
            }
            
            // Just keep the same aspect code depth
            aspect_code_depth_stack.dup(0);
        }
    }
    
    private void pushTag(int tag, int env_id, Instruction inst) {
        // Get env id -> tag stack map
        IntHashMap stack_map = (IntHashMap) inst.getStorageSpace(
                this.dynamic_tags_index);
        if (stack_map == null) {
            stack_map = new IntHashMap(7);
            inst.setStorageSpace(this.dynamic_tags_index, stack_map);
        }
        
        IntStack tags_stack = (IntStack) stack_map.get(env_id);
        if (tags_stack == null) {
            // Create a new stack
            tags_stack = new IntStack();
            tags_stack.push(this.getStaticKindTag(inst));
            stack_map.put(env_id, tags_stack);
        }
        
        tags_stack.push(tag);
    }
    
    public void unpropagate(ExecutionContext context) {
        ThreadEntity thread = context.getThread();
        int env_id = thread.getID();
        MethodEntity me = context.getMethod();

        // Pop the current tag from all instructions (for this thread)
        Method m = me.getMethod();
        if (m == null) {
            return;
        }

        Code c = m.getCode();
        if (c != null) {
            Instruction[] insts = c.getInstructions();
            for (int i = 0; i < insts.length; i++) {
                Instruction inst = insts[i];
                IntHashMap stack_map
                        = (IntHashMap) inst.getStorageSpace(
                                    this.dynamic_tags_index);
                IntStack tag_stack = (IntStack) stack_map.get(env_id);
                tag_stack.pop();
            }
        }
        
        // Get the aspect code depth stack for this thread
        IntStack aspect_code_depth_stack
                = (IntStack) this.env_id_to_depth_stacks.get(
                        env_id);
        aspect_code_depth_stack.pop();
    }
}
    
