package starj.coffer;

public abstract class AbstractMethodrefConstant extends MemberrefConstant {
    public AbstractMethodrefConstant(byte tag, int class_index,
            int name_and_type_index) {
        super(tag, class_index, name_and_type_index);
    }
}
