package starj.coffer;

import java.io.*;

public class LazyConstantFactory extends ConstantFactory {
    private static LazyConstantFactory instance;

    public static ConstantFactory getInstance() {
        if (instance == null) {
            instance = new LazyConstantFactory();
        }
        
        return instance;
    }
    
    public LazyConstantFactory() {
        super();
    }

    protected Constant parse(byte tag, DataInput input) {
        try {
            switch (tag) {
                case Constant.CONSTANT_Utf8: 
                    {
                        int length = input.readUnsignedShort();
                        byte[] info = new byte[length + 2];
                        info[0] = (byte) ((length >> 8) & 0x000000FF);
                        info[1] = (byte) (length & 0x000000FF);
                        input.readFully(info, 2, length);
                        return new LazyConstant(tag, info);
                    }
                case Constant.CONSTANT_Long:
                case Constant.CONSTANT_Double:
                    {
                        byte info[] = new byte[8];
                        input.readFully(info);
                        return new LazyConstant(tag, info);
                    }
                default:
                    return super.parse(tag, input);
            }
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated file");
        }
    }
}
