package starj.toolkits.aspects;

class OffsetTagPair implements Comparable {
    private int offset;
    private int tag;

    public OffsetTagPair(int offset, int tag) {
        this.offset = offset;
        this.tag = tag;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getTag() {
        return this.tag;
    }

    public int compareTo(Object obj) {
        return this.offset - ((OffsetTagPair) obj).offset;
    }

    public boolean equals(Object obj) {
        return this.compareTo(obj) == 0;
    }

    public String toString() {
        return "(" + this.getOffset() + ", " + this.getTag() + ")";
    }
}
