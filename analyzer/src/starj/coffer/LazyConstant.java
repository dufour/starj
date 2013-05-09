package starj.coffer;

import java.io.*;

public class LazyConstant extends Constant {
    private byte[] info;

    public LazyConstant(byte tag, byte[] info) {
        super(tag);
        this.info = info;
    }

    public byte[] getInfo() {
        return this.info;
    }

    public Constant concretize() {
        DataInput input
                = new DataInputStream(new ByteArrayInputStream(this.info));
        try {
            switch (this.getTag()) {
                case Constant.CONSTANT_Utf8:
                    return new Utf8Constant(input.readUTF());
                case Constant.CONSTANT_Long:
                    return new LongConstant(input.readLong());
                case Constant.CONSTANT_Double:
                    return new DoubleConstant(input.readDouble());
                default:
                    throw new RuntimeException("Unknown lazy constant tag");
            }
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }

    boolean isConcrete() {
        return false;
    }
}
