package starj.coffer;

import starj.coffer.types.*;

public abstract class FieldInstruction extends ConstantPoolInstruction {
    public FieldInstruction(short opcode, int length, int offset,
            ConstantPool cp, int index) {
        super(opcode, length, offset, cp, index);
    }
    
    public Type getType() {
        FieldrefConstant field_ref = (FieldrefConstant) this.getConstant();
        ConstantPool cp = this.getConstantPool();
        int name_type_index= field_ref.getNameAndTypeIndex();
        NameAndTypeConstant name_type
                = (NameAndTypeConstant) cp.get(name_type_index);
        String type_encoding = cp.getUtf8(name_type.getDescriptorIndex());
        return TypeRepository.v().getType(type_encoding);
    }
}

