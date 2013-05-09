package starj.coffer;

public class IfIcmpgeInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "if_icmpge";

    public IfIcmpgeInstruction(int offset, int target) {
        super(Code.IF_ICMPGE, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

