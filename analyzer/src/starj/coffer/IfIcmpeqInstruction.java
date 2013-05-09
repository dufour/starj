package starj.coffer;

public class IfIcmpeqInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "if_icmpeq";

    public IfIcmpeqInstruction(int offset, int target) {
        super(Code.IF_ICMPEQ, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

