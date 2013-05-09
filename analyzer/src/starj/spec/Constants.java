package starj.spec;

public interface Constants {
    /** Magic number for *J Spec Files */
    public final static int MAGIC = 0x5AB1E5E5;
    public final static short MINOR_VERSION = ((short) 0);
    public final static short MAJOR_VERSION = ((short) 1);

    /* ==================================================================== * 
     *                         StarJ spec bit masks                         * 
     * ==================================================================== */
    public final static int FIELD_RECORDED               = 0x00000001;
    public final static int FIELD_COUNTED                = 0x00000002;
    public final static int FIELD_ENV_ID                 = 0x00000004;

    /* Arena Delete/New  Event */
    public final static int FIELD_ARENA_ID               = 0x00000008;
    public final static int FIELD_ARENA_NAME             = 0x00000010;

    /* Class Load Event */
    public final static int FIELD_CLASS_NAME             = 0x00000008;
    public final static int FIELD_SOURCE_NAME            = 0x00000010;
    public final static int FIELD_NUM_INTERFACES         = 0x00000020;
    public final static int FIELD_NUM_METHODS            = 0x00000040;
    public final static int FIELD_METHODS                = 0x00000080;
    public final static int FIELD_NUM_STATIC_FIELDS      = 0x00000100;
    public final static int FIELD_STATICS                = 0x00000200;
    public final static int FIELD_NUM_INSTANCE_FIELDS    = 0x00000400;
    public final static int FIELD_INSTANCES              = 0x00000800;
    public final static int FIELD_CLASS_LOAD_CLASS_ID    = 0x00001000;

    /* Class Load Hook Event */
    public final static int FIELD_CLASS_DATA_LEN         = 0x00000008;
    public final static int FIELD_CLASS_DATA             = 0x00000010;

    /* Class Unload Event */
    public final static int FIELD_CLASS_UNLOAD_CLASS_ID  = 0x00000008;

    /* Compiled Method Load/Unload Event */
    public final static int FIELD_METHOD_ID              = 0x00000008;
    public final static int FIELD_CODE_SIZE              = 0x00000010;
    public final static int FIELD_CODE                   = 0x00000020;
    public final static int FIELD_LINENO_TABLE_SIZE      = 0x00000040;
    public final static int FIELD_LINENO_TABLE           = 0x00000080;

    /* Date Dump/Reset Request Event */

    /* GC Finish Event */
    public final static int FIELD_USED_OBJECTS           = 0x00000008;
    public final static int FIELD_USED_OBJECT_SPACE      = 0x00000010;
    public final static int FIELD_TOTAL_OBJECT_SPACE     = 0x00000020;

    /* GC Start */

    /* JNI (Weak) Globalref Alloc/Free */
    public final static int FIELD_REF_ID                 = 0x00000008;
    public final static int FIELD_OBJ_ID                 = 0x00000010;

    /* JVM Init Done / Shut Down */

    /* Method Entry / Entry 2 */
    //Method ID defined in Compiled Method Load
    //Obj ID defined in JNI Globalref Alloc

    /* Monitor ___ */
    public final static int FIELD_OBJECT                 = 0x00000008;
    public final static int FIELD_TIMEOUT                = 0x00000010;
    
    /* Monitor Dump */
    public final static int FIELD_DATA_LEN               = 0x00000008;
    public final static int FIELD_DATA                   = 0x00000010;
    public final static int FIELD_NUM_TRACES             = 0x00000020;
    public final static int FIELD_TRACES                 = 0x00000040;

    /* Object Alloc */
    //Arena ID defined in Arena Delete
    //Obj ID defined in Globalref Alloc
    public final static int FIELD_IS_ARRAY               = 0x00000020;
    public final static int FIELD_SIZE                   = 0x00000040;
    public final static int FIELD_OBJECT_ALLOC_CLASS_ID  = 0x00000080;

    /* Object Dump */
    //Data Len defined in Monitor Dump
    //Data defined in Monitor Dump

    /* Object Free*/
    //Obj ID defined in JNI Globalref Alloc

    /* Object Move */
    //Arena ID defined in Arena Delete
    // Obj ID defined in JNI Globalref Alloc
    public final static int FIELD_NEW_ARENA_ID           = 0x00000020;
    public final static int FIELD_NEW_OBJ_ID             = 0x00000040;


    /* Raw Monitor ____ */
    public final static int FIELD_NAME                   = 0x00000008;
    public final static int FIELD_ID                     = 0x00000010;

    /* Thread End Event */

    /* Thread Start Event */
    public final static int FIELD_THREAD_NAME            = 0x00000008;
    public final static int FIELD_GROUP_NAME             = 0x00000010;
    public final static int FIELD_PARENT_NAME            = 0x00000020;
    public final static int FIELD_THREAD_ID              = 0x00000040;
    public final static int FIELD_THREAD_ENV_ID          = 0x00000080;

    /* Instruction Start Event */
    //Method ID defined in Compild Method Load
    public final static int FIELD_OFFSET                 = 0x00000010;
    public final static int FIELD_IS_TRUE                = 0x00000020;
    public final static int FIELD_KEY                    = 0x00000040;
    public final static int FIELD_LOW                    = 0x00000080;
    public final static int FIELD_HI                     = 0x00000100;
    public final static int FIELD_CHOSEN_PAIR_INDEX      = 0x00000200;
    public final static int FIELD_PAIRS_TOTAL            = 0x00000400;
}
