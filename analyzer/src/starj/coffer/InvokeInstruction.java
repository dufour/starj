package starj.coffer;

public abstract class InvokeInstruction extends ConstantPoolInstruction {
    private Method target_method;
    public InvokeInstruction(short opcode, int length, int offset,
            ConstantPool cp, int index) {
        super(opcode, length, offset, cp, index);
        this.target_method = null;
    }

    public Method getTargetMethod() {
        if (this.target_method == null) {
            AbstractMethodrefConstant method_ref
                    = (AbstractMethodrefConstant) this.getConstant();
            ConstantPool cp = this.getConstantPool();
            String class_name = cp.getClassName(method_ref.getClassIndex());
            ClassFile class_file = Repository.v().lookup(class_name);
            if (class_file != null) {
                NameAndTypeConstant name_and_type = (NameAndTypeConstant)
                        cp.get(method_ref.getNameAndTypeIndex());
                this.target_method = class_file.lookupMethod(
                        cp.getUtf8(name_and_type.getNameIndex()),
                        cp.getUtf8(name_and_type.getDescriptorIndex())
                );
            }
        }
        
        return this.target_method;
    }

    /** 
     * Determines whether an invoke instruction is dynamically or statically
     * resolved.
     * 
     * @return <code>true</code> if this invoke instruction is dynamically
     * resolved (virtual/interface); <code>false</code> otherwise
     * (static/special).
     */
    public abstract boolean isDynamic();

    public String toString() {
        return super.toString() + " // " + getTargetMethod();
    }
}

