package starj.toolkits.aspects;

import starj.coffer.*;

public class AspectTagAttributeRegistrar {
    private AspectTagAttributeRegistrar() {
        // no instances
    }
    
    public static void registerAttributes() {
        AttributeFactory factory
                = Repository.v().getClassFileFactory().getAttributeFactory();
        factory.registerAttribute(AspectKindTagAttribute.ATTRIBUTE_NAME,
                new AspectKindTagAttributeParser());
        factory.registerAttribute(AspectShadowTagAttribute.ATTRIBUTE_NAME,
                new AspectShadowTagAttributeParser());
        factory.registerAttribute(AspectSourceTagAttribute.ATTRIBUTE_NAME,
                new AspectSourceTagAttributeParser());
        factory.registerAttribute(AspectProceedMethodAttribute.ATTRIBUTE_NAME,
                new AspectProceedMethodAttributeParser());
    }
}
