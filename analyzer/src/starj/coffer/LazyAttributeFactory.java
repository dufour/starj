package starj.coffer;

import java.io.*;

public class LazyAttributeFactory extends AttributeFactory {
    private static LazyAttributeFactory instance;

    public static AttributeFactory getInstance() {
        if (instance == null) {
            instance = new LazyAttributeFactory();
        }

        return instance;
    }

    private static LazyAttributeParser parser;

    public LazyAttributeFactory() {
        super();

        if (parser == null) {
            parser = new LazyAttributeParser();
        }
        
        registerAttribute(CodeAttribute.ATTRIBUTE_NAME, parser);
        registerAttribute(InnerClassesAttribute.ATTRIBUTE_NAME, parser);
        registerAttribute(LineNumberTableAttribute.ATTRIBUTE_NAME, parser);
        registerAttribute(LocalVariableTableAttribute.ATTRIBUTE_NAME, parser);
    }
}

class LazyAttributeParser implements AttributeParser {
    public Attribute parseAttribute(int name_index, int length, DataInput input,
            ConstantPool cp, AttributeFactory factory) {
        try {
            byte[] data = new byte[length];
            input.readFully(data);
            return new LazyAttribute(cp, name_index, data);
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }
}
