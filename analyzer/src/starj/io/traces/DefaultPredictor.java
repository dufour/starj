package starj.io.traces;

import starj.coffer.Instruction;
import starj.coffer.BranchInstruction;
import starj.coffer.Code;

public class DefaultPredictor implements InstructionPredictor {
    private static DefaultPredictor instance = new DefaultPredictor();
    
    public static DefaultPredictor v() {
        return instance;
    }

    private DefaultPredictor() {
        // No instance
    }

    public Instruction predict(Instruction inst) {
        Instruction result = null;
        if (inst == null) {
            throw new RuntimeException("Predictor received a null instruction");
        }

        switch (inst.getOpcode()) {
            case Code.GOTO:
            case Code.GOTO_W:
            case Code.IF_ICMPLT:
                return ((BranchInstruction) inst).getTarget();
            default:
                result = inst.getNext();
                break;
        }

        if (result == null) {
            /* Predict this instruction to be executed again.
             * (this is not as silly as it sounds, since in is
             * the cheapest prediction, and in most (normal) cases
             * there is no way to get a better prediction without
             * predicting accross method boundaries. */
            result = inst;
        }

        return result;
   }
}
