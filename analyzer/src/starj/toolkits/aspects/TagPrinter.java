package starj.toolkits.aspects;

import java.io.PrintStream;

import starj.EventBox;
import starj.dependencies.*;
import starj.events.*;
import starj.toolkits.printers.ExtendedEventPrinter;

public class TagPrinter extends ExtendedEventPrinter {
    public TagPrinter(String name, String description) {
        super(name, description);
    }

    public TagPrinter(String name, String description, PrintStream out) {
        super(name, description, out);
    }

    public OperationSet operationDependencies() {
        OperationSet set = super.operationDependencies();
        set.add(AspectTagResolver.v());

        return set;
    }
    
    public void apply(EventBox box) {
        super.apply(box);
        Event event = box.getEvent();
        int id = event.getID();

        switch (id) {
            case Event.INSTRUCTION_START: {
                    InstructionStartEvent e = (InstructionStartEvent) event;
                    int tag = AspectTagResolver.v().getCurrentKindTag(
                            e.getInstruction(), e.getEnvID());
                    if (tag != AspectTagResolver.ASPECT_TAG_NO_TAG
                            && tag != AspectTagResolver.ASPECT_TAG_BASE_CODE) {
                        this.out.println("    Tag => "
                                + AspectTagResolver.TAG_NAMES[tag]);
                    }
                }
                break;
        }
    }
}
