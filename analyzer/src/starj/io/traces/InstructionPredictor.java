package starj.io.traces;

import starj.coffer.Instruction;

public interface InstructionPredictor {
    public Instruction predict(Instruction inst);
}
