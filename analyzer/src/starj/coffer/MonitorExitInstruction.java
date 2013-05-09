package starj.coffer;

public class MonitorExitInstruction extends MonitorInstruction {
    public static final String OPCODE_NAME = "monitorexit";

    public MonitorExitInstruction(int offset) {
        super(Code.MONITOREXIT, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

