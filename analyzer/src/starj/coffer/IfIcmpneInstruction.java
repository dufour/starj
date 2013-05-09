package starj.coffer;

public class IfIcmpneInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "if_icmpne";

    public IfIcmpneInstruction(int offset, int target) {
        super(Code.IF_ICMPNE, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

