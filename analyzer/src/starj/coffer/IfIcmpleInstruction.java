package starj.coffer;

public class IfIcmpleInstruction extends BranchInstruction implements
        IntInstruction {
    public static final String OPCODE_NAME = "if_icmple";

    public IfIcmpleInstruction(int offset, int target) {
        super(Code.IF_ICMPLE, 3, offset, target);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

