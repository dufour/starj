package starj.coffer;

import java.io.DataInput;

public class ClassFileFactory {
    private ConstantFactory const_factory;
    private AttributeFactory attr_factory;

    public ClassFileFactory() {
        this(LazyConstantFactory.getInstance(),
                AttributeFactory.getInstance());
    }

    public ClassFileFactory(ConstantFactory const_factory) {
        this(const_factory, AttributeFactory.getInstance());
    }

    public ClassFileFactory(AttributeFactory attr_factory) {
        this(LazyConstantFactory.getInstance(), attr_factory);
    }

    public ClassFileFactory(ConstantFactory const_factory,
            AttributeFactory attr_factory) {
        this.const_factory = const_factory;
        this.attr_factory = attr_factory;
    }

    public AttributeFactory getAttributeFactory() {
        return this.attr_factory;
    }
    
    public ConstantFactory getConstantFactory() {
        return this.const_factory;
    }

    public void setAttributeFactory(AttributeFactory attr_factory) {
        this.attr_factory = attr_factory;
    }

    public void setConstantFactory(ConstantFactory const_factory) {
        this.const_factory = const_factory;
    }


    public ClassFile newClassFile(byte[] b) {
        return new ClassFile(b, this.const_factory, this.attr_factory);
    }

    public ClassFile newClassFile(byte[] b, ConstantFactory const_factory) {
        return new ClassFile(b, const_factory, this.attr_factory);
    }

    public ClassFile newClassFile(byte[] b, AttributeFactory attr_factory) {
        return new ClassFile(b, this.const_factory, attr_factory);
    }

    public ClassFile newClassFile(byte[] b, ConstantFactory const_factory,
            AttributeFactory attr_factory) {
        return new ClassFile(b, const_factory, attr_factory);
    }

    public ClassFile newClassFile(DataInput input) {
        return new ClassFile(input, this.const_factory, this.attr_factory);
    }

    public ClassFile newClassFile(DataInput input,
            ConstantFactory const_factory) {
        return new ClassFile(input, const_factory, this.attr_factory);
    }

    public ClassFile newClassFile(DataInput input,
            AttributeFactory attr_factory) {
        return new ClassFile(input, this.const_factory, attr_factory);
    }

    public ClassFile newClassFile(DataInput input,
            ConstantFactory const_factory, AttributeFactory attr_factory) {
        return new ClassFile(input, const_factory, attr_factory);
    }
}
