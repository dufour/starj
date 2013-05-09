package starj.coffer;

import java.io.*;

public class ConstantPool {
    private Constant[] cp_data;

    public ConstantPool(DataInput input, ConstantFactory factory) {
        try {
            int cp_length = input.readUnsignedShort();
            Constant[] cp = new Constant[cp_length];
            for (int i = 1; i < cp_length; i++) {
                Constant c = factory.parse(input);
                cp[i] = c;
                byte tag = c.getTag();
                if (tag == Constant.CONSTANT_Long
                        || tag == Constant.CONSTANT_Double) {
                    // Skip one entry in the array (Section 4.4.5 -- JVM Spec)
                    i++;
                }
            }
            this.cp_data = cp;
        } catch (IOException e) {
            throw new ClassFileFormatException("Truncated class file");
        }
    }

    public Constant getLazy(int index) {
        try {
            return this.cp_data[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public Constant get(int index) {
        Constant c = this.cp_data[index];
        if (c != null && !c.isConcrete()) {
            c = ((LazyConstant) c).concretize();
            this.cp_data[index] = c;
        }

        return c;
    }

    public String getUtf8(int index) {
        Utf8Constant c = (Utf8Constant) this.get(index);
        return c.getValue();
    }

    public String getClassName(int index) {
        ClassConstant c = (ClassConstant) this.get(index);
        String tmp_name = this.getUtf8(c.getNameIndex());
        return tmp_name.replace('/', '.');
    }

    public int size() {
        return this.cp_data.length;
    }
}
