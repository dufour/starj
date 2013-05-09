package starj.coffer;

public class FieldrefConstant extends MemberrefConstant {
    public FieldrefConstant(int class_index, int name_and_type_index) {
        super(Constant.CONSTANT_Fieldref, class_index, name_and_type_index);
    }
}
