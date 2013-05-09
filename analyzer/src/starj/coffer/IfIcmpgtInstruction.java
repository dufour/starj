package starj.coffer;

public class IfIcmpgtInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "if_icmpgt";

    public IfIcmpgtInstruction(int offset, int target) {
        super(Code.IF_ICMPGT, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

