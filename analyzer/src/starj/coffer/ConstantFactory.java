package starj.coffer;

import java.io.*;

public class ConstantFactory {
    private static ConstantFactory instance;

    public static ConstantFactory getInstance() {
        if (instance == null) {
            instance = new ConstantFactory();
        }
        
        return instance;
    }

    public ConstantFactory() {
        
    }
    
    public Constant parse(DataInput input) {
        try {
            byte tag = input.readByte();
            return parse(tag, input);
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }
    
    protected Constant parse(byte tag, DataInput input) {
        try {
            switch (tag) {
                case Constant.CONSTANT_Utf8: 
                    return new Utf8Constant(input.readUTF());
                case Constant.CONSTANT_Integer:
                    return new IntegerConstant(input.readInt());
                case Constant.CONSTANT_Float:
                    return new FloatConstant(input.readFloat());
                case Constant.CONSTANT_Long:
                    return new LongConstant(input.readLong());
                case Constant.CONSTANT_Double:
                    return new DoubleConstant(input.readDouble());
                case Constant.CONSTANT_Class:
                    return new ClassConstant(input.readUnsignedShort());
                case Constant.CONSTANT_String:
                    return new StringConstant(input.readUnsignedShort());
                case Constant.CONSTANT_Fieldref:
                    return new FieldrefConstant(input.readUnsignedShort(), 
                            input.readUnsignedShort());
                case Constant.CONSTANT_Methodref:
                    return new MethodrefConstant(input.readUnsignedShort(), 
                            input.readUnsignedShort());
                case Constant.CONSTANT_InterfaceMethodref:
                    return new InterfaceMethodrefConstant(input.readUnsignedShort(), 
                            input.readUnsignedShort());
                case Constant.CONSTANT_NameAndType:
                    return new NameAndTypeConstant(input.readUnsignedShort(), 
                            input.readUnsignedShort());
                default:
                    // Should not happen
                    throw new ClassFileFormatException("Unknown constant pool tag: " + tag);
            }
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated file");
        }
    }
}
