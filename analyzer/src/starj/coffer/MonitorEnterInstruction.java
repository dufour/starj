package starj.coffer;

public class MonitorEnterInstruction extends MonitorInstruction {
    public static final String OPCODE_NAME = "monitorenter";

    public MonitorEnterInstruction(int offset) {
        super(Code.MONITORENTER, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

