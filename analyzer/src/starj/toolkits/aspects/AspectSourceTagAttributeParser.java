package starj.toolkits.aspects;

import java.io.DataInput;

import starj.coffer.*;

public class AspectSourceTagAttributeParser extends AspectTagAttributeParser {
    public Attribute parseAttribute(int name_index, int length,
            DataInput input, ConstantPool constant_pool,
            AttributeFactory factory) {
        return new AspectSourceTagAttribute(constant_pool,
                this.parsePairs(length, input));
    }
}
