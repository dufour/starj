package starj.coffer;

public class IfIcmpltInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "if_icmplt";

    public IfIcmpltInstruction(int offset, int target) {
        super(Code.IF_ICMPLT, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

