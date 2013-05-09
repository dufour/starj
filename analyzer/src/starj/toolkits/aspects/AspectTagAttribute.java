package starj.toolkits.aspects;

import starj.coffer.*;

public abstract class AspectTagAttribute extends Attribute {
    private OffsetTagPair[] pairs;
    
    public AspectTagAttribute(String name, ConstantPool cp,
            OffsetTagPair[] pairs) {
        super(name, cp);
        this.pairs = pairs;
    }
    
    public int getTag(int offset) {
        return this.getTag(offset, AspectTagResolver.ASPECT_TAG_NO_TAG);
    }
    
    public int getTag(int offset, int default_tag) {
        OffsetTagPair[] pairs = this.pairs;
        int rv = default_tag;
        for (int i = 0; i < pairs.length; i++) {
            OffsetTagPair pair = pairs[i];
            
            if (offset < pair.getOffset()) {
                return rv;
            }

            rv = pair.getTag();
        }
        
        return rv;
    }
}
