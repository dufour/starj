package starj.coffer;

public class DcmplInstruction extends Instruction implements
        DoubleInstruction {
    public static final String OPCODE_NAME = "dcmpl";

    public DcmplInstruction(int offset) {
        super(Code.DCMPL, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

