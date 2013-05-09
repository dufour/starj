package starj.coffer;

public class MethodrefConstant extends AbstractMethodrefConstant {
    public MethodrefConstant(int class_index, int name_and_type_index) {
        super(Constant.CONSTANT_Methodref, class_index, name_and_type_index);
    }
}
