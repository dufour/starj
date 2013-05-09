package starj.coffer;

public abstract class MemberrefConstant extends Constant {
    private int class_index;
    private int name_and_type_index;

    public MemberrefConstant(byte tag, int class_index,
            int name_and_type_index) {
        super(tag);
        this.class_index = class_index;
        this.name_and_type_index = name_and_type_index;
    }

    public int getClassIndex() {
        return this.class_index;
    }

    public int getNameAndTypeIndex() {
        return this.name_and_type_index;
    }
}
