package starj.coffer;

import java.io.DataInput;

public interface AttributeParser {
    public Attribute parseAttribute(int name_index, int length, DataInput input,
            ConstantPool constant_pool, AttributeFactory factory);
}
