package starj.coffer;

import java.io.*;

public class LazyCode extends Code {
    private byte[] code;
    private ConstantPool cp;

    public LazyCode(byte[] code, ConstantPool cp) {
        super();
        this.code = code;
        this.cp = cp;
    }

    public void concretize() {
        if (this.code != null) {
            DataInput input
                = new DataInputStream(new ByteArrayInputStream(this.code));
            try {
                this.parseCodeArray(this.code.length, input, this.cp);
                this.code = null; // Make array available for GC
            } catch (IOException e) {
                throw new ClassFileFormatException("Truncated class file");
            }
        }
    }

    public boolean isConcrete() {
        return (this.code == null);
    }
}
