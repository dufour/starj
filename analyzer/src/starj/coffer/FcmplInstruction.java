package starj.coffer;

public class FcmplInstruction extends Instruction implements
        FloatInstruction {
    public static final String OPCODE_NAME = "fcmpl";

    public FcmplInstruction(int offset) {
        super(Code.FCMPL, 1, offset);
    }

    public String getOpcodeName() {
        return OPCODE_NAME;
    }
}

