package starj.toolkits.aspects;

import starj.coffer.*;

public class AspectSourceTagAttribute extends AspectTagAttribute {
    public static final String ATTRIBUTE_NAME = "ca.mcgill.sable.InstructionSource";
    
    public AspectSourceTagAttribute(ConstantPool cp, OffsetTagPair[] pairs) {
        super(AspectSourceTagAttribute.ATTRIBUTE_NAME, cp, pairs);
    }
}
