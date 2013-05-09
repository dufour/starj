package starj.toolkits.aspects;

import starj.coffer.*;

public class AspectShadowTagAttribute extends AspectTagAttribute {
    public static final String ATTRIBUTE_NAME = "ca.mcgill.sable.InstructionShadow";
    
    public AspectShadowTagAttribute(ConstantPool cp, OffsetTagPair[] pairs) {
        super(AspectShadowTagAttribute.ATTRIBUTE_NAME, cp, pairs);
    }
}

