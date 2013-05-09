package starj.io.traces;

import java.io.IOException;
import java.util.*;

public class InstructionPredictorAttribute extends TraceAttribute {
    private String predictor_name;
    private static Map predictors;

    static {
        predictors = new HashMap();
        predictors.put("default", DefaultPredictor.v());
    }
    
    public InstructionPredictorAttribute(String name, int length,
            TraceInput input) throws TraceIOException, IOException {
        super(name, length);
        this.predictor_name = input.readUTF();
        if (!predictors.containsKey(this.predictor_name)) {
            throw new TraceFormatException("Unknown predictor name: '"
                    + this.predictor_name + "'");
        }
    }

    public InstructionPredictor getInstructionPredictor() {
        return (InstructionPredictor) predictors.get(this.predictor_name);
    }

    public static void registerPredictor(String name,
            InstructionPredictor predictor) {
        predictors.put(name, predictor);
    }
}
