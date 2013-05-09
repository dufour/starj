package starj.coffer;

public class InnerClass {
    private int inner_class_info_index;
    private int outer_class_info_index;
    private int inner_name_index;
    private short access_flags;

    public InnerClass(int inner_class_info_index, int outer_class_info_index,
            int inner_name_index, short access_flags) {
        this.inner_class_info_index = inner_class_info_index;
        this.outer_class_info_index = outer_class_info_index;
        this.inner_name_index = inner_name_index;
        this.access_flags = access_flags;
    }

    public int getInnerClassInfoIndex() {
        return this.inner_class_info_index;
    }

    public int getOuterClassInfoIndex() {
        return this.outer_class_info_index;
    }

    public int getInnerNameIndex() {
        return this.inner_name_index;
    }

    public int getAccessFlags() {
        return this.access_flags;
    }
}
