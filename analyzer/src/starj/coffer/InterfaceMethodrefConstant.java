package starj.coffer;

public class InterfaceMethodrefConstant extends AbstractMethodrefConstant {
    public InterfaceMethodrefConstant(int class_index,
            int name_and_type_index) {
        super(Constant.CONSTANT_InterfaceMethodref, class_index,
                name_and_type_index);
    }
}
