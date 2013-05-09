package starj.toolkits.aspects;

import starj.coffer.*;

public class AspectKindTagAttribute extends AspectTagAttribute {
    public static final String ATTRIBUTE_NAME = "ca.mcgill.sable.InstructionKind";
    
    public AspectKindTagAttribute(ConstantPool cp, OffsetTagPair[] pairs) {
        super(AspectKindTagAttribute.ATTRIBUTE_NAME, cp, pairs);
    }
}
