package starj.toolkits.aspects;

import java.io.DataInput;

import starj.coffer.*;

public class AspectProceedMethodAttributeParser implements AttributeParser {
    public Attribute parseAttribute(int name_index, int length,
            DataInput input, ConstantPool constant_pool,
            AttributeFactory factory) {
        if (length != 0) {
            throw new ClassFileFormatException("Invalid attribute length");
        }
        return new AspectProceedMethodAttribute(constant_pool);
    }
}
