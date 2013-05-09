package starj.toolkits.aspects;

import starj.coffer.*;

public class AspectProceedMethodAttribute extends Attribute {
    public static final String ATTRIBUTE_NAME = "ca.mcgill.sable.ProceedMethod";

    public AspectProceedMethodAttribute(ConstantPool cp) {
        super(ATTRIBUTE_NAME, cp);
    }

}
